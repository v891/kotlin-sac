package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO

fun main() {
    println("Task (hide, show, exit):")
    val readLine = readLine()!!

    when (readLine) {
        "hide" -> {
            hideDriver()
        }
        "show" -> println("Obtaining message from image.")
        "exit" -> {
            println("Bye!")
            return
        }
        else -> println("Wrong task: task")
    }

//    main()
}

fun hideDriver() {
    println("Input image file:")
    try {
        val inputFileName = readLine()!!
        val bufferedImage = ImageIO.read(File(inputFileName)) ?: throw FileNotFoundException("")

        val out = hide(bufferedImage)

        println("Output image file:")
        val outputFileName = readLine()!!
        ImageIO.write(out, "png", File(outputFileName))

        println("Input Image: $inputFileName")
        println("Output Image: $outputFileName")
        println("Image $outputFileName is saved.")

    } catch (e: Exception) {
        println("Can't read input file!")
    }
    main()
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

