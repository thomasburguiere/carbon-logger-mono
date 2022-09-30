package ch.burguiere.carbonlog.base

import org.springframework.stereotype.Service

@Service
class HelloService {

    fun message():String {
        return "Hello World!"
    }
}
