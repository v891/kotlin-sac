fun solution(products: List<String>, product: String) {
    // put your code here
    val withIndex: Iterable<IndexedValue<String>> = products.withIndex()

    withIndex
            .filter { it.value == product }
            .map { it.index }
            .joinToString(" ")
            .let(::println)
}