import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.myapplication.NeckAlertReceiver
import com.example.myapplication.NeckStretchingStart
import com.example.myapplication.databinding.FragmentNeckSetBinding
import java.text.DateFormat
import java.util.*


class NeckSetFragment : Fragment() {
    private var _binding: FragmentNeckSetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentNeckSetBinding.inflate(inflater, container, false)
        binding.neckStrechingStart.setOnClickListener {
            val intent = Intent(activity, NeckStretchingStart::class.java)
            startActivity(intent)
        }

        binding.setBtn.setOnClickListener{
            var time = Calendar.getInstance()

            var hour = time.get(Calendar.HOUR)
            var minute = time.get(Calendar.MINUTE)
            var timeListener = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
                    var calendar = Calendar.getInstance()

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    updateTimeText(calendar)

                    startAlarm(calendar)
                }
            }
            var builder = TimePickerDialog(binding.root.context,timeListener,hour,minute,false)
            builder.show()
        }

        return binding.root
    }



    private fun updateTimeText(calendar: Calendar) {
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)

        binding.hourText.text = curTime
    }

    private fun startAlarm(calendar: Calendar){
        var alarmManager : AlarmManager = binding.root.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(binding.root.context, NeckAlertReceiver::class.java)
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
        intent.putExtra("time", curTime)

        var pendingIntent = PendingIntent.getBroadcast(binding.root.context, 1, intent, 0 )

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Log.d("Mytag","확인1")
    }
}