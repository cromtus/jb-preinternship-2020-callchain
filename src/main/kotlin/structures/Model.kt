package structures


data class Model(val root: CallChain) {
    override fun toString() = root.toString()

    fun transform() = root.transform()
}