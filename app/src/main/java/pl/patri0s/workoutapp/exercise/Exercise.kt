package pl.patri0s.workoutapp.exercise

data class Exercise(
    private val id: Int,
    val exerciseName: String,
    val exerciseDesc: Int,
    val exerciseTimeInSecond: Int,
    val exerciseGif: Int,
    var isCompleted: Boolean = false,
    var isSelected: Boolean = false
) {

    fun getId(): Int {
        return id
    }

    fun getName(): String {
        return exerciseName
    }

    fun getDesc(): Int {
        return exerciseDesc
    }

    fun getTime(): Int {
        return exerciseTimeInSecond
    }

    fun getGif(): Int {
        return exerciseGif
    }

    fun getIsCompleted(): Boolean {
        return isCompleted
    }

    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    fun getIsSelected(): Boolean {
        return isSelected
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}