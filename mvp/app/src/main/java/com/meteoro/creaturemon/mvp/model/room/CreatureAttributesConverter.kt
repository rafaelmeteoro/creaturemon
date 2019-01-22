package com.meteoro.creaturemon.mvp.model.room

import androidx.room.TypeConverter
import com.meteoro.creaturemon.mvp.model.CreatureAttributes
import java.util.*

class CreatureAttributesConverter {

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
    fun toCreatureAttributes(value: String?): CreatureAttributes? {
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