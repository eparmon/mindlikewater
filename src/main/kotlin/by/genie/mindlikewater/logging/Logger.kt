package by.genie.mindlikewater.logging

import java.util.logging.Logger
import kotlin.reflect.full.companionObject

// return logger from extended class (or the enclosing class)
fun <T: Any> T.logger(): Logger {
    return Logger.getLogger(unwrapCompanionClass(this.javaClass).name)
}

// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}
