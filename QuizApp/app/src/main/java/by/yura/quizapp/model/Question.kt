package by.yura.quizapp.model

class Question(id: Int, content: String, type: Int, img:String) {

    var id: Int = id
        private set
    var content: String = content
        private set
    var type: Int = type
        private set

    var img: String = img
        private set

    override fun toString(): String {
        return "Question(id=$id, content='$content', type=$type, img='$img')"
    }

}