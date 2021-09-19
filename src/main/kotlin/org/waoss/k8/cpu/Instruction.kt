package org.waoss.k8.cpu

interface Instruction {
    val name: String
    val args: ShortArray?
}

internal data class InstructionImpl(override val name: String, override val args: ShortArray?) : Instruction {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstructionImpl

        if (name != other.name) return false
        if (args != null) {
            if (other.args == null) return false
            if (!args.contentEquals(other.args)) return false
        } else if (other.args != null) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (args?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String = name
}

internal object EmptyInstruction : Instruction {
    override val name: String = ""
    override val args: ShortArray = ShortArray(size = 0)
}

fun constructInstruction(name: String, vararg args: Short): Instruction = InstructionImpl(name, args)

fun emptyInstruction() : Instruction = EmptyInstruction