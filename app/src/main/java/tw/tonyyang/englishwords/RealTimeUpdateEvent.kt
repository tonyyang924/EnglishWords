package tw.tonyyang.englishwords

class RealTimeUpdateEvent(val type: Type) {
    enum class Type {
        UPDATE_WORD_LIST
    }

    var message: String? = null
}