package com.automatica.AXCMP.Principal;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeSince
{

    private String parseDate(String date)
    {
        try{
            String pNDT;
            if(date.length()>=12){
                pNDT = date.substring(6, 8) + "-";

                String month = date.substring(4, 6);
                switch (month)
                    {
                        case "01":
                            pNDT += "Enero";
                            break;
                        case "02":
                            pNDT += "Febrero";
                            break;
                        case "03":
                            pNDT += "Marzo";
                            break;
                        case "04":
                            pNDT += "Abril";
                            break;
                        case "05":
                            pNDT += "Mayo";
                            break;
                        case "06":
                            pNDT += "Junio";
                            break;
                        case "07":
                            pNDT += "Julio";
                            break;
                        case "08":
                            pNDT += "Agosto";
                            break;
                        case "09":
                            pNDT += "Septiembre";
                            break;
                        case "10":
                            pNDT += "Octubre";
                            break;
                        case "11":
                            pNDT += "Noviembre";
                            break;
                        case "12":
                            pNDT += "Diciembre";
                            break;
                        default:
                            pNDT += "Err";
                            break;
                    }

                pNDT += "-" + date.substring(0, 4) + " ";

                return pNDT;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }


        return null;

    }


    public static String convertDate(String date)
    {
        try{
            String pNDT;
            if(date.length()>=12){
                pNDT = date.substring(6, 8) + "-";

                String month = date.substring(4, 6);
                switch (month)
                    {
                        case "01":
                            pNDT += "Ene";
                            break;
                        case "02":
                            pNDT += "Feb";
                            break;
                        case "03":
                            pNDT += "Mar";
                            break;
                        case "04":
                            pNDT += "Abr";
                            break;
                        case "05":
                            pNDT += "May";
                            break;
                        case "06":
                            pNDT += "Jun";
                            break;
                        case "07":
                            pNDT += "Jul";
                            break;
                        case "08":
                            pNDT += "Ago";
                            break;
                        case "09":
                            pNDT += "Sep";
                            break;
                        case "10":
                            pNDT += "Oct";
                            break;
                        case "11":
                            pNDT += "Nov";
                            break;
                        case "12":
                            pNDT += "Dic";
                            break;
                        default:
                            pNDT += "Err";
                            break;
                    }

                pNDT += "-" + date.substring(0, 4) + " ";

                String _AmPm;
                String hour =  date.substring(8,10);
                if(Integer.valueOf(hour) <12){
                    _AmPm = "a.m.";
                }else{
                    _AmPm = "p.m.";
                }

                switch (hour)
                    {
                        case "00":
                            pNDT += "12";
                            break;
                        case "13":
                            pNDT += "01";
                            break;
                        case "14":
                            pNDT += "02";
                            break;
                        case "15":
                            pNDT += "03";
                            break;
                        case "16":
                            pNDT += "04";
                            break;
                        case "17":
                            pNDT += "05";
                            break;
                        case "18":
                            pNDT += "06";
                            break;
                        case "19":
                            pNDT += "07";
                            break;
                        case "20":
                            pNDT += "08";
                            break;
                        case "21":
                            pNDT += "09";
                            break;
                        case "22":
                            pNDT += "10";
                            break;
                        case "23":
                            pNDT += "11";
                            break;
                        default:
                            pNDT += date.substring(8, 10);
                            break;
                    }
                pNDT += ":" + date.substring(10, 12) + " " + _AmPm;

                Log.i("date",	pNDT);
                return pNDT;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }


        return null;

    }

     static public String getDateTimeSince(String target)
    {
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date _target = dateFormat.parse(target);

            Calendar cal = Calendar.getInstance();
            cal.setTime(_target);
            Calendar now = Calendar.getInstance();
            int yd, md, dd, hd, nd, sd;
            StringBuilder out = new StringBuilder();
            yd = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);;
            md =now.get(Calendar.MONTH) -cal.get(Calendar.MONTH);;
            dd = now.get(Calendar.DATE) - cal.get(Calendar.DATE);;
            hd = now.get(Calendar.HOUR_OF_DAY) - cal.get(Calendar.HOUR_OF_DAY);;
            nd = now.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE);;
            sd= now.get(Calendar.SECOND) - cal.get(Calendar.SECOND);;

            if(md <0){yd--; md +=12;}
            if(dd <0){
                md--;
                dd += getDaysInMonth(now.get(Calendar.MONTH) -1,now.get(Calendar.YEAR));
            }
            if( hd < 0) {dd--; hd += 24;}
            if( nd < 0) {hd--; nd += 60;}
            if( sd < 0) {nd--; sd += 60;}
            if( yd > 0)
                {
                    if( (md >= 2 && md <= 4) || (md >= 7 && md <= 9)) out.append("más de ");
                    if( md == 5 || (md >= 10 && md <= 11)) out.append( "casi ");

                    int _yd = ((md >= 10 && md <= 11) ? (yd + 1) : yd);
                    out.append(  _yd + " año" + (_yd == 1 ? "" : "s") );
                    if( md >= 5 && md <= 9) out.append( " y medio");
                } else if( md > 0) {

                if( (dd >= 7 && dd <= 11) || (dd >= 16 && dd <= 25)) out.append("más de ");
                if( (dd >= 12 && dd <= 15) || (dd >= 26 && dd <= 30)) out.append( "casi ");

                int _md = ((dd >= 26 && dd <= 30) ? (md + 1) : md);
                out.append( _md + " mes" + (_md == 1 ? "" : "es"));
                if( dd >= 12 && dd <= 25) out.append( " y medio");
            }else if( dd > 0) {

                if( hd >= 16 && hd <= 19) out.append("más de ");
                if( hd >= 20 && hd <= 23) out.append( "casi ");

                int _dd = ((hd >= 20 && hd <= 23) ? (dd + 1) : dd);
                out.append( _dd + " día" + (_dd == 1 ? "" : "s"));

                //if( hd >= 16 && hd <= 19) out.push( " y medio");
            }else if( hd > 0){

                if( nd >= 30 && nd <= 49) out.append("más de ");
                if( (nd >= 25 && nd <= 29) || (nd >= 50 && nd <= 59)) out.append( "casi ");

                int _hd = ((nd >= 50 && nd <= 59) ? (hd + 1) : hd);
                out.append( _hd + " hora" + (_hd == 1 ? "" : "s"));

                //if( nd >= 25 && nd <= 49) out.push( " y media");
            }

            else if( nd > 0) out.append( nd + " minuto" + (nd == 1 ? "" : "s"));
            else if( sd > 0) out.append( "unos segundos"); //sd+" segundo"+(sd == 1 ? "" : "s"));
            return out.toString();
        }catch(Exception ex){
            ex.printStackTrace();
        }


        return null;

    }

    private static double getDaysInMonth(int month,int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        Date currmon = calendar.getTime();

        calendar.clear();
        calendar.set(Calendar.MONTH, month+1);
        calendar.set(Calendar.YEAR, year);

        Date nextmon = calendar.getTime();
        return Math.floor((nextmon.getTime() - currmon.getTime())/(24*3600*1000));

    }
}
