package by.yura.quizapp.model

class Category(id: Int, text: String) {
    var id = id
        private set
    var text = text
        private set

    override fun toString(): String {
        return "Category(id=$id, text='$text')"
    }
}