package com.sorkmos.log.audit

import java.security.MessageDigest
import java.time.LocalDateTime

const val FIRST_HASH = "0000000000000000000000000000000000000000"

data class Block(
    val data: String,
    val previousHash: String,
    val hash: String,
    val created: LocalDateTime
) {
    companion object {
        fun initialBLock(data: String): Block {
            return Block(data = data, previousHash = FIRST_HASH, hash = sha1(data + FIRST_HASH), created = LocalDateTime.now())
        }
    }
}


fun Block.createNextBlock(nextData: String): Block {
    return Block(data = nextData, previousHash = this.hash, created = LocalDateTime.now(), hash = sha1(nextData + this.hash))
}

fun Block.isNextBlockValid(nextBlock: Block): Boolean {
    return sha1(nextBlock.data + this.hash) == nextBlock.hash
}

fun validateBlockchain(blocks: List<Block>): List<Pair<Block, Block>> {
    return  blocks.zipWithNext().filter { f -> !f.first.isNextBlockValid(f.second) }
}

fun sha1(data: String): String = generateHash("SHA-1", data)

fun generateHash(type: String, data: String): String {

    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
        .getInstance(type)
        .digest(data.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }
    println("$type: $data -> $result")

    return result.toString()
}