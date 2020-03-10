package com.slack.exercise

import org.mockito.Mockito

/**
 * Helper class to provide static access to handy methods
 */
object TestUtils {

    /**
     * More elegant way of mocking classes for kotlin to avoid ::class.java trailing
     */
    inline fun <reified T> mokk(): T {
        return Mockito.mock(T::class.java)
    }

    const val BLACKLIST: String = "af\n" +
        "ag\n" +
        "aq\n" +
        "ar\n" +
        "aw\n" +
        "aba\n" +
        "ada\n" +
        "aea\n" +
        "aha\n" +
        "aia\n" +
        "ba\n" +
        "bo\n" +
        "bc\n" +
        "be\n" +
        "bd\n" +
        "bi\n" +
        "bba\n" +
        "bry\n" +
        "bre\n" +
        "bri\n" +
        "cb\n" +
        "cc\n" +
        "ce\n" +
        "ci\n" +
        "cam\n" +
        "cat\n" +
        "chr\n" +
        "cou\n" +
        "carin\n" +
        "cara\n" +
        "db\n" +
        "dd\n" +
        "dg\n" +
        "di\n" +
        "du\n" +
        "dave\n" +
        "dar\n" +
        "dan\n" +
        "dou\n" +
        "dea\n" +
        "ev\n" +
        "ea\n" +
        "eu\n" +
        "ed\n" +
        "el\n" +
        "gl\n" +
        "ge\n" +
        "gal\n" +
        "gar\n" +
        "gat\n" +
        "jac\n" +
        "jai\n" +
        "jes\n" +
        "jak\n" +
        "jame\n" +
        "kd\n" +
        "kf\n" +
        "kt\n" +
        "kj\n" +
        "kw\n" +
        "kal\n" +
        "kan\n" +
        "kar\n" +
        "ker\n" +
        "kiel\n" +
        "mi\n" +
        "mat\n" +
        "mag\n" +
        "mah\n" +
        "madd\n" +
        "nad\n" +
        "nag\n" +
        "nai\n" +
        "nak\n" +
        "nam\n" +
        "pi\n" +
        "pc\n" +
        "pt\n" +
        "ps\n" +
        "pk\n" +
        "pac\n" +
        "pag\n" +
        "pai\n" +
        "pan\n" +
        "pap\n" +
        "rog\n" +
        "raf\n" +
        "raj\n" +
        "ram\n" +
        "ran\n" +
        "sf\n" +
        "sh\n" +
        "sl\n" +
        "sn\n" +
        "so\n" +
        "sha\n" +
        "sean\n" +
        "sal\n" +
        "sara\n" +
        "sam"

}