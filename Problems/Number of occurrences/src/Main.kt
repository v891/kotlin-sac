fun solution(strings: List<String>, str: String): Int {
    // put your code here

    var out = 0

    for (s in strings) {
        if (s.equals(str)) {
            out++
        }
    }
    return out
}
