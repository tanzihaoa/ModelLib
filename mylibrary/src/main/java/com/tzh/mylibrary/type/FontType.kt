package com.tzh.mylibrary.type

import androidx.annotation.StringDef

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@MustBeDocumented
@StringDef(FontType.SJXZ)
@Retention(AnnotationRetention.SOURCE)
annotation class FontType {
    companion object{

        /**
         * 三极小篆简 字体
         */
        const val SJXZ = "sjxz"
    }
}