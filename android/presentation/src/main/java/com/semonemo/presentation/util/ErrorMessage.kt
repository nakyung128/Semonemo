package com.semonemo.presentation.util

object ErrorMessage {
    const val EMPTY_MESSAGE = "공백을 제외해주세요"
    const val NICK_TOO_SHORT = "최소 2글자를 입력해주세요"
    const val PW_TOO_SHORT = "최소 10글자를 입력해주세요"
    const val NICK_TOO_LONG = "최대 15글자만 가능해요"
    const val PW_TOO_LONG = "최대 20글자만 가능해요"
    const val NICK_NOT_MATCH = "한글,영어,숫자,특수기호(-,_)만 가능해요"
    const val PW_NOT_MATCH = "영어,숫자만 가능해요"
}
