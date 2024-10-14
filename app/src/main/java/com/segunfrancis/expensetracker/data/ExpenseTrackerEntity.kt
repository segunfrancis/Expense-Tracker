package com.segunfrancis.expensetracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val price: Double,
    val description: String,
    val splitOption: String,
    val image: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpenseEntity

        if (id != other.id) return false
        if (price != other.price) return false
        if (description != other.description) return false
        if (splitOption != other.splitOption) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + splitOption.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}
