package by.yura.quizapp.model

class User(id: Int, name: String, right: Int, wrong: Int, score: Int) {
    var id = id
        private set
    var name = name
        private set
    var right = right
        private set
    var wrong = wrong
        private set
    var score = score
        private set

    constructor(name: String, right: Int, wrong: Int, score: Int) : this(0, name, right, wrong, score)

    override fun toString(): String {
        return "User(id=$id, name='$name', right=$right, wrong=$wrong, score=$score)"
    }
}