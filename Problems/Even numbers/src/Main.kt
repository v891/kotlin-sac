fun solution(numbers: List<Int>) {
    // put your code here

    print(numbers.filter { it % 2 == 0 }.joinToString(" "))

}