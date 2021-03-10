package com.sorkmos.log.audit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BlockTest {

    @Test
    fun `generate hash`() {
        var hash = generateHash("SHA-1", "hej$FIRST_HASH")
        Assertions.assertThat((hash)).isEqualTo("B98B068CD9E6D48C55C3234DCC6BA2BCD5A983E5");
    }


    @Test
    fun `test creating two blocks`() {
        val b1 = Block.initialBLock("hej")
        val b2 = b1.createNextBlock("svejs")

        println("${b1.created} ${b1.data} -> ${b1.hash}")
        println("${b2.created} ${b2.data} -> ${b2.hash}")

        assert(b1.hash == "B98B068CD9E6D48C55C3234DCC6BA2BCD5A983E5")
        assert(b2.hash == "DA2BC3D6998C0A86C938EB1F0DA84185C46F65F6")

        assert(b1.isNextBlockValid(b2))
    }

    @Test
    fun `create one thousand blocks and validate blockchain`() {
        val initBlock = Block.initialBLock("first row")
        val blocks = mutableListOf<Block>(initBlock)

        (1..998).forEach {
            val nextBlock = blocks.last().createNextBlock("string$it")
            blocks.add(nextBlock)
        }

        println("Blocks: ${blocks.size}")

        blocks.zipWithNext { a, b -> println(a.isNextBlockValid(b)) }
        val tamperedBlcoks = validateBlockchain(blocks)
        assert(tamperedBlcoks.isEmpty())
    }

    @Test
    fun `modify block in middle of chain`() {
        val initBlock = Block.initialBLock("first row")
        val blocks = mutableListOf<Block>(initBlock)

        (1..998).forEach {
            val nextBlock = blocks.last().createNextBlock("string$it")
            blocks.add(nextBlock)
        }


        println("Blocks: ${blocks.size}")
        blocks[500] = initBlock

        val tamperedBlocks = validateBlockchain(blocks);
        println(tamperedBlocks.size)
        println(tamperedBlocks.toString().replace(",","\n"))
        assert(tamperedBlocks.size == 2)

    }
}