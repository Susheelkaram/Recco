import com.susheelkaram.recco.recording.ScreenRecorder
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

var appModule = module {
    single {
        ScreenRecorder(androidApplication())
    }
}