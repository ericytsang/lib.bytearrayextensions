package com.github.ericytsang.lib.bytearrayextensions

import java.util.Arrays

infix fun ByteArray.eq(that:ByteArray):Boolean
{
    return Arrays.equals(this,that)
}

infix fun ByteArray.ne(that:ByteArray):Boolean
{
    return !Arrays.equals(this,that)
}

/**
 * returns true if any of the bits in the specified bit field is set to 1; false
 *   otherwise.
 *
 * @param indexOfByte 0-based index of the byte to check.
 * @param mask bit mask to apply to the byte before testing for set bits.
 */
fun ByteArray.getFlag(indexOfByte:Int,mask:Int):Boolean
{
    return (this[indexOfByte].toInt() and mask) != 0
}

/**
 * sets all bits in the mask in the byte at the index of indexOfByte to 1 if
 *   value is true, and 0 otherwise.
 *
 * @param indexOfByte 0-based index of the byte to check.
 * @param mask bit mask to apply to the byte before testing for set bits.
 */
fun ByteArray.setFlag(indexOfByte:Int,mask:Int,value:Boolean)
{
    val oldValue = this[indexOfByte].toInt()

    val newValue = if (value)
    {
        oldValue or mask
    }
    else
    {
        oldValue and mask.inv()
    }

    this[indexOfByte] = newValue.toByte()
}

/**
 * @see ByteArray.getLong(indexOfFirstBit:Int,lengthInBits:Int):Long
 */
fun ByteArray.getInt(indexOfFirstBit:Int,lengthInBits:Int):Int
{
    return getLong(indexOfFirstBit,lengthInBits)
        .and(0x00000000FFFFFFFF)
        .toInt()
}

/**
 * returns a long whose value is extracted from the specified bit range in this
 *   ByteArray. Big Endian.
 *
 * @param indexOfFirstBit index of the first bit in the bit range
 * @param lengthInBits length of the bit range in bits; number of bits in the
 *   bit range.
 */
fun ByteArray.getLong(indexOfFirstBit:Int,lengthInBits:Int):Long
{
    var number = 0L
    val byteOffsets = 0..Math.abs(Math.floor(indexOfFirstBit/8.0)-Math.ceil((indexOfFirstBit+lengthInBits)/8.0)).toInt()-1
    byteOffsets.forEach()
    {
        offset ->

        var currentByte = this[indexOfFirstBit/8+offset].toInt().and(0xFF)
        var shiftBitsLeftAmount = 8

        // if this is the first byte, truncate leading bits
        if (offset == byteOffsets.first)
        {
            val bitsToTruncate = indexOfFirstBit%8
            shiftBitsLeftAmount -= bitsToTruncate
            currentByte = currentByte
                .shl(bitsToTruncate)
                .and(0xFF)
                .ushr(bitsToTruncate)
        }

        // if this is the last byte, truncate trailing bits
        if (offset == byteOffsets.last)
        {
            val bitsToTruncate = (8-(indexOfFirstBit+lengthInBits)%8)%8
            shiftBitsLeftAmount -= bitsToTruncate
            currentByte = currentByte
                .and(0xFF)
                .ushr(bitsToTruncate)
        }

        // add the value to the end result
        number = number
            .shl(shiftBitsLeftAmount)
            .or(currentByte.toLong())
    }
    return number
}

/**
 * sets the bits in the bit range to the value of the bits in value. Big Endian.
 *
 * @param indexOfFirstBit index of the first bit in the bit range
 * @param lengthInBits length of the bit range in bits; number of bits in the
 *   bit range.
 */
fun ByteArray.putNumber(indexOfFirstBit:Int,lengthInBits:Int,value:Long)
{
    // zero out the bits we will modify
    val byteOffsets = 0..Math.abs(Math.floor(indexOfFirstBit/8.0)-Math.ceil((indexOfFirstBit+lengthInBits)/8.0)).toInt()-1
    var shiftBitsRightAmount = lengthInBits
    byteOffsets.forEach()
    {
        offset ->

        val index = indexOfFirstBit/8+offset
        var shiftBitsRightAmountDelta = -8

        // if this is the first byte, zero out trailing bits
        if (offset == byteOffsets.first)
        {
            val bitsToTruncate = Math.abs(8-indexOfFirstBit%8)
            shiftBitsRightAmountDelta += 8-bitsToTruncate
            this[index] = this[index]
                .toInt()
                .ushr(bitsToTruncate)
                .shl(bitsToTruncate)
                .toByte()
        }

        // if this is the last byte, zero out leading bits
        if (offset == byteOffsets.last)
        {
            val bitsToTruncate = ((indexOfFirstBit+lengthInBits)%8).let { if (it == 0) 8 else it }
            //shiftBitsRightAmountDelta += 8-bitsToTruncate
            this[index] = this[index]
                .toInt()
                .shl(bitsToTruncate)
                .ushr(bitsToTruncate)
                .toByte()
        }

        // add the value to the end result
        shiftBitsRightAmount += shiftBitsRightAmountDelta
        this[index] = value
            .ushr(Math.max(shiftBitsRightAmount,0))
            .shl(Math.max(-shiftBitsRightAmount,0))
            .and(0xFF)
            .or(this[index].toLong())
            .toByte()
    }
}