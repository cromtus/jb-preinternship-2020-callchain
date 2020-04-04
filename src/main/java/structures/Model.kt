package structures


data class Model(val root: CallChain) {
    fun transform() = root.transform()
}