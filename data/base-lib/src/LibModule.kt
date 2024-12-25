import org.koin.dsl.module

val libModule = module {
    single<AppDispatcher> { createAppDispatcher() }
}

