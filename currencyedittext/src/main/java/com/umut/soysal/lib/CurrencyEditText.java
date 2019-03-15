package com.umut.soysal.lib;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.Currency;
import java.util.Locale;

@SuppressLint("AppCompatCustomView")
@SuppressWarnings("unused")
public class CurrencyEditText extends EditText
{
    private Locale currencyLocale;

    private Locale defaultLocale = Locale.US;

    private boolean allowNegativeValues = false;

    private long rawValue = 0L;

    private CurrencyTextWatcher textWatcher;
    private String hintCache = null;
    public static int cursor=0;
    private int decimalDigits = 0;
    private int mValueInLowestDenom = 0;


    public CurrencyEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
        processAttributes(context, attrs);
    }



    public void setAllowNegativeValues(boolean negativeValuesAllowed)
    {
        allowNegativeValues = negativeValuesAllowed;
    }


    public boolean areNegativeValuesAllowed()
    {
        return allowNegativeValues;
    }


    public long getRawValue()
    {
        return rawValue;
    }

    public void setValue(long value)
    {
        String formattedText = format(value);
        setText(formattedText);
    }


    public Locale getLocale()
    {
        return currencyLocale;
    }


    public void setLocale(Locale locale)
    {
        currencyLocale = locale;
        refreshView();
    }


    public String getHintString()
    {
        CharSequence result = super.getHint();
        if (result == null) return null;
        return super.getHint().toString();
    }


    public int getDecimalDigits()
    {
        return decimalDigits;
    }


    public void setDecimalDigits(int digits)
    {
        if (digits < 0 || digits > 27)
        {
            throw new IllegalArgumentException("Decimal Digit value must be between 0 and 27");
        }
        decimalDigits = digits;

        refreshView();
    }


    public void configureViewForLocale(Locale locale)
    {
        this.currencyLocale = locale;
        Currency currentCurrency = getCurrencyForLocale(locale);
        decimalDigits = currentCurrency.getDefaultFractionDigits();
        refreshView();
    }


    public void setDefaultLocale(Locale locale)
    {
        this.defaultLocale = locale;
    }


    public Locale getDefaultLocale()
    {
        return defaultLocale;
    }


    public String formatCurrency(String val)
    {
        return format(val);
    }


    public String formatCurrency(long rawVal)
    {
        return format(rawVal);
    }


    private void refreshView()
    {
        setText(format(getRawValue()));
        updateHint();
    }

    private String format(long val)
    {
        return CurrencyTextFormatter.formatText(String.valueOf(val), currencyLocale, defaultLocale, decimalDigits);
    }

    private String format(String val)
    {
        return CurrencyTextFormatter.formatText(val, currencyLocale, defaultLocale, decimalDigits);
    }

    private void init()
    {

        this.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_COMMA)
                {

                }
                else if (keyCode == KeyEvent.KEYCODE_NUMPAD_DOT)
                {

                }
                return false;
            }
        });


        this.setGravity(Gravity.RIGHT);
        this.setInputType(InputType.TYPE_CLASS_TEXT);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(20);
        this.setFilters(filters);

        this.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                changeDecimalKeyboard();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        currencyLocale = retrieveLocale();
        Currency currentCurrency = getCurrencyForLocale(currencyLocale);
        decimalDigits = currentCurrency.getDefaultFractionDigits();
        initCurrencyTextWatcher();
    }

    protected void setValueInLowestDenom(int mValueInLowestDenom)
    {
        this.mValueInLowestDenom = mValueInLowestDenom;
    }

    private void changeDecimalKeyboard()
    {
        this.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    private void changeSignedKeyboard()
    {
        this.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    private void initCurrencyTextWatcher()
    {

        if (textWatcher != null)
        {
            this.removeTextChangedListener(textWatcher);
        }
        textWatcher = new CurrencyTextWatcher(this);
        this.addTextChangedListener(textWatcher);
    }

    private void processAttributes(Context context, AttributeSet attrs)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CurrencyEditText);
        this.hintCache = getHintString();
        updateHint();

        this.setAllowNegativeValues(array.getBoolean(R.styleable.CurrencyEditText_allow_negative_values, false));
        this.setDecimalDigits(array.getInteger(R.styleable.CurrencyEditText_decimal_digits, decimalDigits));

        array.recycle();
    }

    private void updateHint()
    {
        if (hintCache == null)
        {
            setHint(getDefaultHintValue());
        }
    }

    private String getDefaultHintValue()
    {
        try
        {
            return Currency.getInstance(currencyLocale).getSymbol();
        }
        catch (Exception e)
        {
            Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using locale '%s', falling back to defaultLocale", currencyLocale));
            try
            {
                return Currency.getInstance(defaultLocale).getSymbol();
            }
            catch (Exception e1)
            {
                Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using default locale '%s', falling back to USD", defaultLocale));
                return Currency.getInstance(Locale.US).getSymbol();
            }

        }
    }

    private Locale retrieveLocale()
    {
        Locale locale;
        try
        {
            locale = getResources().getConfiguration().locale;
        }
        catch (Exception e)
        {
            Log.w("CurrencyEditText", String.format("An error occurred while retrieving users device locale, using fallback locale '%s'", defaultLocale), e);
            locale = defaultLocale;
        }
        return locale;
    }

    private Currency getCurrencyForLocale(Locale locale)
    {
        Currency currency;
        try
        {
            currency = Currency.getInstance(locale);
        }
        catch (Exception e)
        {
            try
            {
                Log.w("CurrencyEditText", String.format("Error occurred while retrieving currency information for locale '%s'. Trying default locale '%s'...", currencyLocale, defaultLocale));
                currency = Currency.getInstance(defaultLocale);
            }
            catch (Exception e1)
            {
                Log.e("CurrencyEditText", "Both device and configured default locales failed to report currentCurrency data. Defaulting to USD.");
                currency = Currency.getInstance(Locale.US);
            }
        }
        return currency;
    }

    protected void setRawValue(long value)
    {
        rawValue = value;
    }
}