package by.yura.quizapp.model

class Answer(id: Int, content: String, questionId: Int, complexity: Int, right: Boolean) {
    var id = id
        private set
    var content = content
        private set
    var questionId = questionId
        private set
    var complexity = complexity
        private set
    var right = right
        private set

    override fun toString(): String {
        return "Answer(id=$id, content='$content', questionId=$questionId, complexity=$complexity, right=$right)"
    }


}