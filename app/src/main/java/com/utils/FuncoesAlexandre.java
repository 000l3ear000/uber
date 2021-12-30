/*
@Autor  : Alexandre Abade
@Class  : FuncoesAlexandre
@Date   : 25/10/2021
@contatc: +55(69)99387-7484
 */
package com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.utils.CommonUtilities.OriginalDateFormate;

public class FuncoesAlexandre {
    //padrao americano para padrao Brasileiro
    public static String dataFormatadaUS_PT(String dt){
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = formato.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data!=null){
            formato.applyPattern("dd/MM/yyyy");
            dt = formato.format(data);
        } else
            dt="";
        return  dt;
    }
    //padrao BRL para Americano
    public static String dataFormatadaPT_US( String dt){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        String sreturn;
        try {
            data = formato.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data!=null){
            formato.applyPattern("yyyy-MM-dd");
            sreturn = formato.format(data);
        } else
            sreturn="";

        //sreturn =FormataStringDT_andTIme(dt);
        return  sreturn;
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix var2 = new Matrix();
        switch(orientation) {
            case 1:
                return bitmap;
            case 2:
                var2.setScale(-1.0F, 1.0F);
                break;
            case 3:
                var2.setRotate(180.0F);
                break;
            case 4:
                var2.setRotate(180.0F);
                var2.postScale(-1.0F, 1.0F);
                break;
            case 5:
                var2.setRotate(90.0F);
                var2.postScale(-1.0F, 1.0F);
                break;
            case 6:
                var2.setRotate(90.0F);
                break;
            case 7:
                var2.setRotate(-90.0F);
                var2.postScale(-1.0F, 1.0F);
                break;
            case 8:
                var2.setRotate(-90.0F);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap var3 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), var2, true);
            bitmap.recycle();
            return var3;
        } catch (OutOfMemoryError var4) {
            var4.printStackTrace();
            return bitmap;
        }
    }

    public static String decodeFileAlexandre(String path, int DESIREDWIDTH, int DESIREDHEIGHT, String tempImgName, Context actContext) {
        String var5 = null;
        Bitmap image = null;

        try {
            int var7 = Utils.getExifRotation(path);
            Bitmap var8 = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);
            if (var8.getWidth() <= DESIREDWIDTH && var8.getHeight() <= DESIREDHEIGHT) {
                if (var8.getWidth() > var8.getHeight()) {
                    image = ScalingUtilities.createScaledBitmap(var8, var8.getHeight(), var8.getHeight(), ScalingUtilities.ScalingLogic.CROP);
                } else {
                    image = ScalingUtilities.createScaledBitmap(var8, var8.getWidth(), var8.getWidth(), ScalingUtilities.ScalingLogic.CROP);
                }
            } else {
                image = ScalingUtilities.createScaledBitmap(var8, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);
            }

            image = rotateBitmap(image, var7);
            String pathImg="";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {

                pathImg= actContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)+"" ;
            }
            else
            {

                pathImg=Environment.getExternalStorageDirectory()
                        .toString() ;
            }
            String var9 = pathImg;
            File var10 = new File(var9 + "/" + "TempImages");
            if (!var10.exists()) {
                var10.mkdir();
            }

            File var11 = new File(var10.getAbsolutePath(), tempImgName);
            var5 = var11.getAbsolutePath();
            FileOutputStream var12 = null;

            try {
                var12 = new FileOutputStream(var11);
                image.compress(Bitmap.CompressFormat.JPEG, 60, var12);
                var12.flush();
                var12.close();
            } catch (FileNotFoundException var14) {
                var14.printStackTrace();
            } catch (Exception var15) {
                var15.printStackTrace();
            }

            image.recycle();
        } catch (Throwable var16) {
        }

        return var5 == null ? path : var5;
    }

    private static String FormataStringDT_andTIme(String data) {
        String dia = data.split("/")[0];
        String mes = data.split("/")[1];
        String ano = data.split("/")[2];

        return ano + '-' + mes+'-'+dia;
        // Utilizo o .slice(-2) para garantir o formato com 2 digitos.
    }
}
