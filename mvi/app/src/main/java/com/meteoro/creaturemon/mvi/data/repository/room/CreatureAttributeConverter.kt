package com.meteoro.creaturemon.mvi.data.repository.room

import androidx.room.TypeConverter
import com.meteoro.creaturemon.mvi.data.model.CreatureAttributes
import java.util.*

class CreatureAttributeConverter {

    @TypeConverter
    fun fromCreatureAttributes(attributes: CreatureAttributes?): String? {
        if (attributes != null) {
            return String.format(
                Locale.US,
                "%d,%d,%d",
                attributes.intelligence,
                attributes.strength,
                attributes.endurance
            )
        }
        return null
    }

    @TypeConverter
    fun toCreateAttributes(value: String?): CreatureAttributes? {
        if (value != null) {
            val pieces = value.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return CreatureAttributes(
                java.lang.Integer.parseInt(pieces[0]),
                java.lang.Integer.parseInt(pieces[1]),
                java.lang.Integer.parseInt(pieces[2])
            )
        }
        return null
    }
}