package by.yura.quizapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import by.yura.quizapp.listener.FirebaseListener
import by.yura.quizapp.model.Answer
import by.yura.quizapp.model.Category
import by.yura.quizapp.model.Question
import by.yura.quizapp.model.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import org.json.JSONObject

class DBHelper private constructor(context: Context) :
    SQLiteAssetHelper(context, "Quiz.db", null, 1) {

    private var db: SQLiteDatabase = this.writableDatabase

    val storage = Firebase.storage
    val referenceFromUrl = storage.getReferenceFromUrl("gs://quizapp-879f2.appspot.com/")

    companion object {
        private var instance: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (instance == null)
                instance = DBHelper(context)
            return instance as DBHelper
        }
    }


    fun getUsers(firebaseListener: FirebaseListener): List<User> {

        /*val userList = ArrayList<User>()
        val cursor = db.rawQuery("SELECT * FROM user;", null)
        while (cursor.moveToNext()) {
            userList.add(
                User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getInt(cursor.getColumnIndex("right")),
                    cursor.getInt(cursor.getColumnIndex("wrong")),
                    cursor.getInt(cursor.getColumnIndex("score"))
                )
            )
        }*/

        database = Firebase.database
        val reference = database.getReference("Players")
        var userList: ArrayList<User> = ArrayList()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children
                var name: String
                var score: String

                for (item in list) {
                    name = JSONObject(item.value.toString()).getString("Name")
                    score = JSONObject(item.value.toString()).getString("Score")
                    Log.i("TAG", "getUsers: " + name + " " + score)
                    userList.add(User(name, 0, 0, score.toInt()))

                }


                firebaseListener.update(userList)
            }

        })

        Log.i("TAG", "getUsers: " + userList)

        return userList
    }

    var database = Firebase.database


    fun getCategories(firebaseListener: FirebaseListener): List<Any> {
//        val categoryList = ArrayList<Category>()
        /*val cursor = db.rawQuery("SELECT * FROM category;", null)

        while (cursor.moveToNext()) {
            categoryList.add(
                Category(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title"))
                )
            )
        }*/

        database = Firebase.database
        val reference = database.getReference("category")
        var list: ArrayList<Category> = ArrayList()

        reference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var value1 = snapshot.children //as ArrayList<String>


                for (item in value1) {
                    val v = JSONObject((item.value as HashMap<*, *>).toString()).getString("title")
                    Log.i("MYLOG", "onDataChange: " + item.key + " " + item.value)
                    list.add(Category(item.key!!.toInt(), v))
                }

                Log.i("MYLOG", "onDataChange: $list")

                firebaseListener.update(list)

            }

        })

        return list
    }

    fun getQuestions(type: Int): List<Question> {
        val questionList = ArrayList<Question>()
        val cursor = db.rawQuery("SELECT * FROM question WHERE category_id = $type;", null)

        while (cursor.moveToNext()) {
            questionList.add(
                Question(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getInt(cursor.getColumnIndex("category_id")), ""
                )
            )
        }

        return questionList
    }

    fun getQuestionsFireBase(firebaseListener: FirebaseListener) {
        val questionList = ArrayList<Question>()

//        val database = Firebase.database
        val reference = database.getReference("question")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children
                var content: String

                for (item in list) {
                    content =
                        JSONObject(item.value.toString().replace(' ', '_')).getString("content")
                            .replace('_', ' ')
                    questionList.add(Question(item.key!!.toInt(), content, 0, ""))

                }

                firebaseListener.update(questionList)

            }
        })


    }

    val questionList = ArrayList<Question>()
    val answerList = ArrayList<Answer>()

    fun getQuestionsFFireBase(firebaseListener: FirebaseListener) {

        val database = Firebase.database
        val reference = database.getReference("questionf")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {


                val list = snapshot.children
                var counter: Int = 0
                var content: String = ""
                var img: String = ""
                var answerContent: String = ""
                var answerStatus: String = ""

                for (question in list) {

                    counter++
                    content = JSONObject(question.value.toString()).getString("content")
                    img = JSONObject(question.value.toString()).getString("img")
                    questionList.add(Question(question.key!!.toInt(), content, 0, img))

                    val jsonArray = JSONObject(question.value.toString()).getJSONArray("answer")
                    for (i in 1..4) {
                        answerContent = JSONObject(jsonArray[i].toString()).getString("content")
                        answerStatus = JSONObject(jsonArray[i].toString()).getString("status")
                        answerList.add(
                            Answer(
                                counter, answerContent, question.key!!.toInt(), 0,
                                answerStatus.toInt() == 1
                            )
                        )
                    }

                }

                Log.i("TAG", "onDataChange: " + questionList)
//                Log.i("TAG", "onDataChange: " + answerList)

                //Thread(Runnable {
                loadImage(firebaseListener)
                //})

//                firebaseListener.update(questionList)

            }

        })

    }

    var images: Array<Bitmap?> = arrayOfNulls<Bitmap?>(10)


    fun loadImage(firebaseListener: FirebaseListener) {

//        var questionItem: Question? = null
//
//        for (item in questionList) {
//            if (item.id == id) {
//                questionItem = item
//                break
//            }
//        }
//
//        var decode:Bitmap? = null
//
//        val child = referenceFromUrl.child(questionItem!!.img)
//
//        child.getBytes(1024 * 1024)
//            .addOnSuccessListener { byteArray ->
//                decode = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
//
//            }.addOnFailureListener{
//                Log.i("MYLOG", "onCreateView:d ")
//            }


        /* var count = 1

         for (item in questionList) {
             val child = referenceFromUrl.child(item.img)
             Log.i("TAG", "onCreateView: " + item.img)


             child.getBytes(1024 * 1024)
             .addOnSuccessListener { byteArray ->
                 Log.i("TAG", "onCreateView:s ")
                 count++
                 images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))

                 if (count == 11) {
                     firebaseListener.update(questionList)
                 }

             }.addOnFailureListener{
                     count++

                     Log.i("TAG", "onCreateView:d ")

                     if (count == 11) {
                         firebaseListener.update(questionList)
                     }
             }

 //            Thread.sleep(1000)

         }*/

        var count = 1

        Thread(Runnable {
            while (true) {
                Log.i("TAG", "loadImage: " + count)
                Thread.sleep(1000)
                if (count == 11) {
                    Log.i("TAG", "loadImage: ")

                    firebaseListener.update(questionList)
                    break
                }
            }
        }).start()

        referenceFromUrl.child(questionList[0].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
                //images.add(0, BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[0] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                count++
            }

        referenceFromUrl.child(questionList[1].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(1, BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[1] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[2].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(2, BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[2] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[3].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[3] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[4].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[4] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[5].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[5] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[6].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[6] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[7].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[7] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[8].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[8] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }

        referenceFromUrl.child(questionList[9].img).getBytes(1024 * 1024)
            .addOnSuccessListener { byteArray ->
                Log.i("TAG", "onCreateView:s ")
//                images.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size))
                images[9] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

                count++
            }


    }

    fun getImage(id: Int): Bitmap? {
//        Log.i("TAG", "getImage: " + images.size)
        return images[id - 1]
    }

    fun getAnswers(questionId: Int, complexity: Array<String>): List<Answer> {
        val answerList = ArrayList<Answer>()
        val cursor = db.rawQuery(
            "SELECT * FROM answer WHERE question_id = $questionId AND complexity IN (?,?);",
            complexity
        )

        while (cursor.moveToNext()) {
            answerList.add(
                Answer(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getInt(cursor.getColumnIndex("question_id")),
                    cursor.getInt(cursor.getColumnIndex("complexity")),
                    intToBoolean(cursor.getInt(cursor.getColumnIndex("right")))
                )
            )
        }

        return answerList
    }

    fun getAnswerF(id: Int): List<Answer> {

        val tempList = ArrayList<Answer>()

        for (item in answerList) {
            if (item.questionId == id) {
                tempList.add(item)
            }
        }

        return tempList
    }

    private fun intToBoolean(int: Int): Boolean {
        return int != 0
    }

    fun deleteAllUsers() {
        db.delete("user", null, null)
    }

    var count = 0;
    var flag = true

    fun insertUser(player: User, firebaseListener: FirebaseListener) {

        var ch = object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val list = snapshot.children
                var name: String
                var score: String


                for (item in list) {
                    name = JSONObject(item.value.toString()).getString("Name")
                    score = JSONObject(item.value.toString()).getString("Score")
                    Log.i("TAG", "getUsers: " + name + " " + score)
                    count++
                }

                Log.i("TAG", "getUsers: " + count)
                flag = false

            }

        }

        database = Firebase.database
        val reference = database.getReference("Players")
        reference.addValueEventListener(ch)

        Thread(Runnable {

            while (flag) {

            }

            reference.removeEventListener(ch)
            Log.i("TAG", "onCreateView: " + count)
            val database1 = Firebase.database
            val reference1 = database1.getReference("Players").child((count + 1).toString())
            reference1.child("Name").setValue(player.name)
            reference1.child("Score").setValue(player.score)
             var countii = 0

            reference1.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("TAG", "onCreateViewmove: " + count)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("TAG", "onCreateViewch: " + count)
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.i("TAG", "onCreateViewadd: " + countii)
                    countii++
                    if(countii == 2) {
                        firebaseListener.update(null)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Log.i("TAG", "onCreateViewre: " + count)
                }

            })

            count = 0

        }).start()
/* reference.addValueEventListener(object : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {

    }

    override fun onDataChange(snapshot: DataSnapshot) {




        val list = snapshot.children
        var name: String
        var score: String


            for (item in list) {
                name = JSONObject(item.value.toString()).getString("Name")
                score = JSONObject(item.value.toString()).getString("Score")
                Log.i("TAG", "getUsers: " + name + " " + score)
                count++
            }

        flag = false

    }

})*/

/*Thread(Runnable {

    while (flag) {

    }

    reference.removeEventListener()
    Log.i("TAG", "onCreateView: " + count)
    val database1 = Firebase.database
    val reference1 = database1.getReference("Players").child((count+1).toString())
    reference1.child("Name").setValue(player.name)
    reference1.child("Score").setValue(player.score)

    reference1.addChildEventListener(object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {

        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {

        }

    })

    count = 0
    firebaseListener.update(null)
}).start()

//        reference.child("Name").setValue(player.name)
//        reference.child("Score").setValue(player.score)


/*val contentValues = ContentValues()
contentValues.put("name", player.name)
contentValues.put("right", player.right)
contentValues.put("wrong", player.wrong)
contentValues.put("score", player.score)
db.insert("user", null, contentValues)*/*/
    }
}