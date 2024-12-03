package ru.example.project999.core

import android.os.Build
import android.os.Bundle
import java.io.Serializable

//ТУТ ПИШЕМ ОБЁРТКУ НАД БАНДЛОМ...ВИДИМО, ДЛЯ ТЕСТОВ

/*Сериализация ( Serialize , в последующем «сохранение») – это процесс сохранения данных объекта во внешнем хранилище.
Эта операция работает в паре с обратной – восстановлением данных, называемой десереализацией ( Deserealize , в последующем
 «восстановление»)
Serializable – это маркерный интерфейс, который не требует реализации каких-либо методов; он просто указывает JVM,
что объект можно сериализовать. Externalizable, с другой стороны, предоставляет больший контроль над сериализацией,
позволяя разработчикам явно определить какие данные сохранять и как их восстанавливать.*/
interface SaveAndRestoreState<T : Serializable> {

    interface Save<T : Serializable> {
        fun save(data: T)
    }

    //тут пример interface segregation`a->вместо написания функции isEmpty() мы наследуемся от инт-са IsEmpty()
    interface Restore<T : Serializable> : IsEmpty {
        fun restore(): T
    }

    interface Mutable<T : Serializable> : Save<T>, Restore<T>

    abstract class Abstract<T : Serializable>(
        private val bundle: Bundle?, //единственное, что приходит от андроида - здесь обёртка над андроидовским бандлом
        private val key: String,
        private val clasz: Class<T>
    ) : SaveAndRestoreState.Mutable<T> {

        override fun isEmpty(): Boolean = bundle == null

        //чё делает этот метод ?
        //этот метод сохраняет в бандл твой кэш, сериазбл - то есть ты туда можешь положить любое значение, реализующее
        //интерфейс сериалазбл
        override fun save(data: T) {
            bundle!!.putSerializable(key, data)
        }

        override fun restore(): T {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle!!.getSerializable(key, clasz)!!
            } else {
                bundle!!.getSerializable(key)!! as T
            }
        }
    }
}