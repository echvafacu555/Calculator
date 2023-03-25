package echevasoft.calculaterp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import echevasoft.calculaterp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var addOperation=false
    private var addDecimal=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun numberAction(view: View)
    {
        if (view is Button) {
            if (view.text==".")
            {
                if (addDecimal)
                    binding.workingsTV.append(view.text)
                    addDecimal=false
                    addOperation=false
            }else
                    binding.workingsTV.append(view.text)
                    addOperation=true
    }
}
    fun operationAction(view: View)
    {
        if (view is Button && addOperation)
        {
            binding.workingsTV.append(view.text)
            addOperation=false
            addDecimal=true
        }
        }


    fun calculateResults():String {

        val digitsOperation = digitsOperation()
        if (digitsOperation.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperation)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float

                if (operator == '+')
                    result += nextDigit

                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
     {
        var list = passedList
        while ( list.contains('÷')  || list.contains('x'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv (passedList: MutableList<Any>): MutableList<Any>
     {
        val newList =  mutableListOf<Any>()
        var restartIndex =passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when (operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex= i+1
                    }

                    '÷'->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex= i+1
                    }
                    else->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }

            }
        if (i > restartIndex)
            newList.add(passedList[i])
    }
        return newList
    }

    fun digitsOperation(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""

        for (character in binding.workingsTV.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "") {
            list.add(currentDigit.toFloat())
        }
        return list
    }

    fun clearOneDigit(view: View)
    {
        val length = binding.workingsTV.length()
        if (length>0)
        {
            binding.workingsTV.text=binding.workingsTV.text.subSequence(0, length-1)
        }
    }

    fun clearAll(view: View)
    {
        binding.resultsTV.text = ""
        binding.workingsTV.text = ""
    }

    fun equalsAction(view: View)
    {
        binding.resultsTV.text = calculateResults()

    }
}


