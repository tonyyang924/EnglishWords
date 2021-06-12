package tw.tonyyang.englishwords.ui.exam

data class ExamData(val trueWord: String, val wordMean: String, val answers: Array<String>, val trueAnswerIndex: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExamData

        if (trueWord != other.trueWord) return false
        if (wordMean != other.wordMean) return false
        if (!answers.contentEquals(other.answers)) return false
        if (trueAnswerIndex != other.trueAnswerIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trueWord.hashCode()
        result = 31 * result + wordMean.hashCode()
        result = 31 * result + answers.contentHashCode()
        result = 31 * result + trueAnswerIndex
        return result
    }
}