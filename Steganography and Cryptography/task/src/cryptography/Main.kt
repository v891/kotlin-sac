package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

fun main() {
    println("Task (hide, show, exit):")

    when (readLine()!!) {
        "hide" -> hideDriver()
        "show" -> showDriver()
        "exit" -> {
            println("Bye!")
            return
        }
        else -> println("Wrong task: task")
    }
}

fun showDriver() {
    println("Input image file:")

    try {
        val inputFileName = readLine()!!
        val bufferedImage = ImageIO.read(File(inputFileName)) ?: throw FileNotFoundException("")
                ?: throw FileNotFoundException("")

        println(showMessage(bufferedImage))
        println("Message:")
    } catch (e: IOException) {
        println("Can't read input file!")
    }
    main()
}

fun hideDriver() {
    println("Input image file:")
    try {
        val inputFileName = readLine()!!
        val bufferedImage = ImageIO.read(File(inputFileName))
                ?: throw FileNotFoundException("")

        println("Output image file:")
        val outputFileName = readLine()!!

        println("Message to hide:")
        val map = message()

        (map as MutableList).addAll(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1))

        if (map.size > bufferedImage.width * bufferedImage.height) {
            throw RuntimeException("The input image is not large enough to hold this message.")
        }

        val out = hideMessage(bufferedImage, map)

        ImageIO.write(out, "png", File(outputFileName))
        println("Message saved in $outputFileName image.")

    } catch (e: RuntimeException) {
        println(e.message)
    } catch (e: IOException) {
        println("Can't read input file!")
    }
    main()
}

private fun message(): List<Int> {
    val message = readLine()!!
    val messageBitList: MutableList<Byte> = message.encodeToByteArray().toMutableList()
    val map = toBits(messageBitList)

    val passwordBits = passwordBits(message.length)

    return xored(map, passwordBits)
}

private fun passwordBits(messageLength: Int): List<Int> {
    println("Password:")
    val password = readLine()!!
    val toMutableList =
            password.repeat(messageLength / password.length + 1).substring(0, messageLength).encodeToByteArray()
                    .toMutableList()

    return toBits(toMutableList)
}

private fun xored(map: List<Int>, passwordBits: List<Int>) =
        map.mapIndexed { index, _ -> map[index] xor passwordBits[index] }

private fun toBits(message: MutableList<Byte>): List<Int> {
    val map = message.map {
        Integer.toBinaryString(it.toInt()).padStart(8, '0')
    }.map { it ->
        it.toCharArray().map {
            Character.getNumericValue(it)
        }
    }.flatten()
    return map
}

fun hideMessage(inputImage: BufferedImage, message: List<Int>): BufferedImage {
    var arrIndex = 0
    for (j in 0 until inputImage.height) {
        for (i in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(i, j))

            if (arrIndex >= message.size) {
                break
            }
            val rgb = Color(
                    color.red,
                    color.green,
                    modifyBit(color.blue, message[arrIndex++])
            ).rgb

            inputImage.setRGB(i, j, rgb)
        }
    }
    return inputImage
}

fun showMessage(inputImage: BufferedImage): String {
    val endList = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1)
    val checker = mutableListOf<Int>()

    val out: MutableList<Int> = mutableListOf<Int>()

    outer@ for (j in 0 until inputImage.height) {
        for (i in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(i, j))
            val element = color.blue % 2

            if (checker.size == 24) {
                Collections.rotate(checker, -1)
                checker[23] = element
            } else {
                checker.add(element)
            }

            if (checker == endList) {
                break@outer
            }
            out.add(element)
        }
    }
    val list = out.subList(0, out.size - 23)

    val passwordBits = passwordBits(list.size)
    val collect = collect(xored(list, passwordBits).toMutableList())
    return collect.toString(Charsets.UTF_8)
}

fun collect(list: MutableList<Int>): ByteArray/*: List<MutableList<Int>>*/ {

    val pageSize = 8

    val map: List<Int> = (0 until ((list.size + pageSize - 1) / pageSize))
            .map { i: Int ->
                list.subList(
                        i * pageSize,
                        Math.min(pageSize * (i + 1), list.size)
                )
            }.map {
                it.joinToString("").toInt(2)
            }

    val array = ByteArray(map.size)
    for (i in map.indices) {
        array[i] = map[i].toByte()
    }

    return array
}

fun hide(inputImage: BufferedImage): BufferedImage {

    for (i in 0 until inputImage.width) {
        for (j in 0 until inputImage.height) {
            val color = Color(inputImage.getRGB(i, j))

            val rgb = Color(
                    changeLeastToOne(color.red),
                    changeLeastToOne(color.green),
                    changeLeastToOne(color.blue)
            ).rgb

            inputImage.setRGB(i, j, rgb)
        }
    }
    return inputImage
}

fun changeLeastToOne(input: Int): Int {
    return if (input % 2 == 0) input + 1 else input
}

fun modifyBit(n: Int, b: Int): Int {
    val mask = 1 shl 0
    return n and mask.inv() or (b shl 0 and mask)
}