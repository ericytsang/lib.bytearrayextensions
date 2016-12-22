package com.github.ericytsang.lib.bytearrayextensions

import org.junit.Test

/**
 * Created by root on 26/09/16.
 */
class ByteArrayExtensionsTest
{
    val byteArray1 = byteArrayOf(0x00,0x00,0x00,0x01,0x01,0x00,0x00,0x00)
    val byteArray2 = byteArrayOf(0x04,-0x80)
    val byteArray3 = byteArrayOf(-1,-1,-1)

    @Test
    fun getFlagTest()
    {
        // only 0x04 bit is set to 1
        assert(!byteArray2.getFlag(0,0x00))
        assert(!byteArray2.getFlag(0,0x01))
        assert(!byteArray2.getFlag(0,0x02))
        assert(byteArray2.getFlag(0,0x04))
        assert(!byteArray2.getFlag(0,0x08))
        assert(!byteArray2.getFlag(0,0x10))
        assert(!byteArray2.getFlag(0,0x20))
        assert(!byteArray2.getFlag(0,0x40))
        assert(!byteArray2.getFlag(0,-0x80))

        // only 0x80 bit is set to 1
        assert(!byteArray2.getFlag(1,0x00))
        assert(!byteArray2.getFlag(1,0x01))
        assert(!byteArray2.getFlag(1,0x02))
        assert(!byteArray2.getFlag(1,0x04))
        assert(!byteArray2.getFlag(1,0x08))
        assert(!byteArray2.getFlag(1,0x10))
        assert(!byteArray2.getFlag(1,0x20))
        assert(!byteArray2.getFlag(1,0x40))
        assert(byteArray2.getFlag(1,-0x80))

        // return true if at least one bit in mask is 1
        assert(byteArray2.getFlag(0,0xFF))
        assert(byteArray2.getFlag(1,0xFF))

        // return false if all bits in mask are 0
        assert(!byteArray2.getFlag(0,0xFB))
        assert(!byteArray2.getFlag(1,0x7F))
    }

    @Test
    fun setFlagTest()
    {
        // all bits are 0
        assert(!byteArray1.getFlag(0,0xFF))

        // set bit 0x04 to 1
        byteArray1.setFlag(0,0x04,true)

        // only bit 0x04 is 1
        assert(!byteArray1.getFlag(0,0xFB))
        assert(byteArray1.getFlag(0,0x04))

        // set bit 0x04 to 0
        byteArray1.setFlag(0,0x04,false)

        // all bits are 0
        assert(!byteArray1.getFlag(0,0xFF))
    }

    @Test
    fun getLongTest1()
    {
        val actual = byteArrayOf(0x00,0x00,0x96.toByte(),0x23).getLong(0,4*8)
        val expected = 0x00009623L
        assert(actual == expected) {"$actual == $expected"}
    }

    @Test
    fun getIntTest1()
    {
        assert(byteArray1.getInt(4*8+1,1*8) == 0x02)
        assert(byteArray1.getInt(4*8+2,1*8) == 0x04)
        assert(byteArray1.getInt(4*8+3,1*8) == 0x08)
        assert(byteArray1.getInt(4*8+4,1*8) == 0x10)

        assert(byteArray1.getInt(4*8+4,1*8-1) == 0x08)
        assert(byteArray1.getInt(4*8+4,1*8-2) == 0x04)
        assert(byteArray1.getInt(4*8+4,1*8-3) == 0x02)
        assert(byteArray1.getInt(4*8+4,1*8-4) == 0x01)

        assert(byteArray1.getInt(4*8,1*8) == 0x01)
        assert(byteArray1.getInt(4*8,2*8) == 0x0100)
        assert(byteArray1.getInt(4*8,3*8) == 0x010000)
        assert(byteArray1.getInt(4*8,4*8) == 0x01000000)
        assert(byteArray1.getInt(3*8,5*8) == 0x01000000)
        assert(byteArray1.getInt(3*8,4*8) == 0x01010000)
        assert(byteArray1.getInt(2*8,4*8) == 0x00010100)
        assert(byteArray1.getInt(1*8,4*8) == 0x00000101)
    }

    @Test
    fun getIntTest2()
    {
        assert(byteArray3.getInt(0,1)  == 0b1) {"${byteArray3.getInt(0,1)}  == ${0b1}"}
        assert(byteArray3.getInt(0,2)  == 0b11) {"${byteArray3.getInt(0,2)}  == ${0b11}"}
        assert(byteArray3.getInt(0,3)  == 0b111) {"${byteArray3.getInt(0,3)}  == ${0b111}"}
        assert(byteArray3.getInt(0,4)  == 0b1111) {"${byteArray3.getInt(0,4)}  == ${0b1111}"}
        assert(byteArray3.getInt(0,5)  == 0b11111) {"${byteArray3.getInt(0,5)}  == ${0b11111}"}
        assert(byteArray3.getInt(0,6)  == 0b111111) {"${byteArray3.getInt(0,6)}  == ${0b111111}"}
        assert(byteArray3.getInt(0,7)  == 0b1111111) {"${byteArray3.getInt(0,7)}  == ${0b1111111}"}
        assert(byteArray3.getInt(0,8)  == 0b11111111) {"${byteArray3.getInt(0,8)}  == ${0b11111111}"}
        assert(byteArray3.getInt(0,9)  == 0b111111111) {"${byteArray3.getInt(0,9)}  == ${0b111111111}"}
        assert(byteArray3.getInt(0,10) == 0b1111111111) {"${byteArray3.getInt(0,10)} == ${0b1111111111}"}
        assert(byteArray3.getInt(0,11) == 0b11111111111)
        assert(byteArray3.getInt(0,12) == 0b111111111111)
        assert(byteArray3.getInt(0,13) == 0b1111111111111)
        assert(byteArray3.getInt(0,14) == 0b11111111111111)
        assert(byteArray3.getInt(0,15) == 0b111111111111111)
        assert(byteArray3.getInt(0,16) == 0b1111111111111111)
    }

    @Test
    fun getLongTest()
    {
        assert(byteArray1.getLong(4*8,1*8) == 0x01L)
        assert(byteArray1.getLong(4*8,2*8) == 0x0100L)
        assert(byteArray1.getLong(4*8,3*8) == 0x010000L)
        assert(byteArray1.getLong(4*8,4*8) == 0x01000000L)
        assert(byteArray1.getLong(3*8,5*8) == 0x0101000000L)
        assert(byteArray1.getLong(3*8,4*8) == 0x01010000L)
        assert(byteArray1.getLong(2*8,4*8) == 0x00010100L)
        assert(byteArray1.getLong(1*8,4*8) == 0x00000101L)
    }

    @Test
    fun setNumberTest1()
    {
        // all bits are 0
        assert(byteArray1.getInt(0*8,2*8) == 0)

        // set bits 0x0105 to 1
        byteArray1.putNumber(0*8,2*8,0x0105)

        // only bits other than 0x0105 are 0
        assert(!byteArray1.getFlag(0,0xFE))
        assert(!byteArray1.getFlag(1,0xFA))

        // bits 0x0105 are 1
        assert(byteArray1.getFlag(0,0x01))
        assert(byteArray1.getFlag(1,0x04))
        assert(byteArray1.getFlag(1,0x01))

        // retrieved value is the same
        assert(byteArray1.getInt(0*8,2*8) == 0x0105)
    }

    @Test
    fun setNumberTest2()
    {
        // all bits are 0
        assert(byteArray1.getInt(0*8,2*8) == 0)

        // set bits 0x0105 to 1
        byteArray1.putNumber(0*8,2*8,0x0105L)

        // only bits other than 0x0105 are 0
        assert(!byteArray1.getFlag(0,0xFE))
        assert(!byteArray1.getFlag(1,0xFA))

        // bits 0x0105 are 1
        assert(byteArray1.getFlag(0,0x01))
        assert(byteArray1.getFlag(1,0x04))
        assert(byteArray1.getFlag(1,0x01))

        // retrieved value is the same
        assert(byteArray1.getInt(0*8,2*8) == 0x0105)
    }

    @Test
    fun setNumberTest3()
    {
        // all bits are 0
        assert(byteArray1.getInt(0*8,2*8) == 0)

        // set bits 0x0001 to 1
        byteArray1.putNumber(0*8+1,2*8,0x0002L)

        // retrieved value is the same
        assert(byteArray1.getInt(0*8,2*8) == 0x0001)

        // only bits other than 0x0001 are 0
        assert(!byteArray1.getFlag(0,0xFF))
        assert(!byteArray1.getFlag(1,0xFE))

        // bits 0x0001 are 1
        assert(byteArray1.getFlag(1,0x01))
    }

    @Test
    fun setNumberTest4()
    {
        // all bits are 0
        assert(byteArray1.getInt(0*8,2*8) == 0)

        // set bits 0x0004 to 1
        byteArray1.putNumber(0*8+1,2*8-2,0x0002L)

        // retrieved value is the same
        assert(byteArray1.getInt(0*8,2*8) == 0x0004)

        // only bits other than 0x0001 are 0
        assert(!byteArray1.getFlag(0,0xFF))
        assert(!byteArray1.getFlag(1,0xFB))

        // bits 0x0004 are 1
        assert(byteArray1.getFlag(1,0x04))
    }

    @Test
    fun eqAndNeTest()
    {
        assert(byteArrayOf(4,2,6,7) eq byteArrayOf(4,2,6,7))
        assert(!(byteArrayOf(4,2,6,7) eq byteArrayOf(4,2,7)))
        assert(byteArray1 ne byteArray2)
        assert(!(byteArray1 ne byteArray1))
    }
}