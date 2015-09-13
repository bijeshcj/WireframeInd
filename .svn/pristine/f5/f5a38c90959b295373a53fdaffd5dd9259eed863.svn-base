package com.verizontelematics.indrivemobile.animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.verizontelematics.indrivemobile.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author bijesh
 */

public class AnimatedView extends ImageView implements View.OnTouchListener{

    private Context mContext;
    int x = -1;
    int y = -1;
    private float startX = -1;
    private float startY = -1;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private Handler h;
    private static final int FRAME_RATE = 10;
    private Paint mPaint;
    private int mCounter;
    private Pt[] myNewNonCircle;
    private ArrayList<TutorialPoint> mListTutorialPoints = new ArrayList<TutorialPoint>();
    private boolean isCircle = false;


    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
        mPaint = getLinePaint(isCircle);
//        initializePoints();
//        myNewNonCircle = initializePoints1();
        this.setOnTouchListener(this);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {

            if(isCircle) {
                appendRegularCirclePoints();
            }else {
                appendIrregularCirclePoints();
            }
            invalidate();
        }
    };
    private int handCounter = 0;
    private boolean isFirst = true;
    private ArrayList<Integer> lstX = new ArrayList<Integer>();
    private ArrayList<Integer> lstY = new ArrayList<Integer>();
    private ArrayList<Pt> lstPt = new ArrayList<Pt>();

    protected void onDraw(Canvas canvas) {

//        if(showNonCircleMessage){
//            showInCorrectMessage(canvas);
////            h.postDelayed(r, 10000);
//            showNonCircleMessage = false;
//        }

        BitmapDrawable hand = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.fpdwn);
        if (isFirst) {
            float[] startPoints = getStartPoints(isCircle);
            startX = startPoints[0];
            startY = startPoints[1];
            isFirst = false;
        }

        Path path = new Path();
        path.moveTo(startX, startY);
        for (TutorialPoint p : mListTutorialPoints) {
            path.lineTo(p.x, p.y);
        }
        if(isCircle){
            mPaint = getLinePaint(isCircle);
        }
        canvas.drawPath(path, mPaint);
        if (handCounter < getIterationLengthOne(isCircle)) {
            TutorialPoint tutPoint = getShapePoints(isCircle,handCounter);
            canvas.drawBitmap(hand.getBitmap(),tutPoint.x, tutPoint.y, null);
            handCounter++;
        }
//        System.out.println("mCount "+mCounter+" isCircle "+isCircle+" isNonCircle "+isNonCircle);
        boolean isNonCircle = false;
        if (mCounter < getIterationLengthOne(isCircle)) {
//            block which draws shape
            h.postDelayed(r, FRAME_RATE);
        }else if(mCounter == getIterationLengthOne(isCircle) && !isCircle && !isNonCircle){
//            block which decides which shape to draw
            mCounter = 0;
            handCounter = 0;
            isCircle = true;
            mListTutorialPoints = new ArrayList<TutorialPoint>();
            boolean showNonCircleMessage = true;
//            showMessage(canvas, true, getMessage1(false), true);
            showToastMessage(getMessage1(false),false);
            h.postDelayed(r, 5000);
        }else if(mCounter == getIterationLengthOne(isCircle) && isCircle && !isNonCircle){
//            block to stop rendering
//            showMessage(canvas, true, getMessage1(true), false);
//            endTutorial();
            showToastMessage(getMessage1(true),true);
        }

    }

    private void endTutorial(){
        h.postDelayed(removeRunnable,5000);
    }

    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
             AnimatedView.this.setVisibility(View.GONE);
        }
    };



    private String getMessage(boolean isCircle){
        StringBuilder builder = new StringBuilder();
//        "The Shape is Incorrect, it should be closer to circle"
        if(!isCircle){
              builder.append("The Shape is Incorrect,").append("\r\n").append("it should be closer to circle");
        }else{
              builder.append("The Shape is closer to circle").append("\r\n").append("Great job.");
        }
        return builder.toString();
    }

    private String getMessage1(boolean isCircle){
        if(!isCircle){
            return Html.fromHtml("The Shape is Incorrect,<br />it should be closer to circle").toString();
        }else{
            return Html.fromHtml("The Shape is closer to circle,<br />Great job.").toString();
        }
    }

    private void showToastMessage(String message,boolean isCircle){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Toast toast = new Toast(mContext);
        View toastView = inflater.inflate(R.layout.toast_layout,null);
        ImageView imgView = (ImageView)toastView.findViewById(R.id.toast_image);
        TextView txtView = (TextView)toastView.findViewById(R.id.toast_txt_message);
        if(!isCircle)
            imgView.setBackgroundResource(R.drawable.wrong);
        else
            imgView.setBackgroundResource(R.drawable.rightshape);
        txtView.setText(message);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        //          toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
//                0, 0);
    }

    private void showMessage(Canvas canvas,boolean show,String message,boolean color){

//        if(show) {
            canvas.drawText(message, 216f, 945f, getTextPaint(!color));
//            showNonCircleMessage = false;
//        }
    }



    private TutorialPoint getShapePoints(boolean isCircle,int whichOrdinal){
        if(isCircle){
            return mRegularCircle[whichOrdinal];
        }else{
            return mIrregularCircle[whichOrdinal];
        }
    }


    private float[] getStartPoints(boolean isCircle){
        float[] startXY = new float[2];
        if(isCircle){
            startXY[0] = mRegularCircle[0].x;
            startXY[1] = mRegularCircle[0].y;
        }else{
            startXY[0] = mIrregularCircle[0].x;
            startXY[1] = mIrregularCircle[0].y;
        }
        return startXY;
    }



    private void appendRegularCirclePoints(){
        if(mCounter < getIterationLength(mRegularCircle))
            mListTutorialPoints.add(mRegularCircle[mCounter++]);
    }

    private void appendIrregularCirclePoints(){
        if(mCounter < getIterationLength(mIrregularCircle))
            mListTutorialPoints.add(mIrregularCircle[mCounter++]);
    }


    private int getIterationLength(TutorialPoint[] arr) {
        return arr.length;
    }
    private int getIterationLengthOne(boolean isCircle){
        if(isCircle)
            return mRegularCircle.length;
        else
            return mIrregularCircle.length;
    }

    private int getRandom() {
        int max = 1024, min = 1;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


    ArrayList<Point> lstPoints = new ArrayList<Point>();


    private Paint getLinePaint(boolean isCircle) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        if(isCircle) {
            paint.setColor(Color.parseColor("#339933"));
        }else{
            paint.setColor(Color.parseColor("#CD5C5C"));
        }
        paint.setStrokeWidth(6f);
        return paint;
    }

    private Paint getTextPaint(boolean color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        if(color) {
            paint.setColor(Color.parseColor("#339933"));
        }else{
            paint.setColor(Color.parseColor("#CD5C5C"));
        }
        paint.setStrokeWidth(3f);
        paint.setTextSize(35f);
        return paint;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return true;
    }

    class Pt {
        int x, y;

        Pt(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }

    class TutorialPoint{
        float x,y;
        TutorialPoint(float x,float y){
            this.x = x;
            this.y = y;
        }
    }

    public AnimatedView(Context context) {
        super(context);
    }

    private TutorialPoint[] mIrregularCircle = {
            new TutorialPoint(487.0f,164.0f),
            new TutorialPoint(487.0f,164.0f),
            new TutorialPoint(487.0f,164.0f),
            new TutorialPoint(481.04883f,165.4878f),
            new TutorialPoint(471.45886f,167.63528f),
            new TutorialPoint(453.92184f,173.35938f),
            new TutorialPoint(431.76865f,184.11566f),
            new TutorialPoint(407.51245f,200.4602f),
            new TutorialPoint(379.89505f,226.16089f),
            new TutorialPoint(361.0f,245.0f),
            new TutorialPoint(344.63123f,267.50287f),
            new TutorialPoint(329.90564f,289.35852f),
            new TutorialPoint(318.29596f,304.90515f),
            new TutorialPoint(310.7667f,315.5266f),
            new TutorialPoint(304.2406f,325.1391f),
            new TutorialPoint(298.86765f,337.68628f),
            new TutorialPoint(292.96295f,352.59265f),
            new TutorialPoint(288.62488f,367.9171f),
            new TutorialPoint(285.33258f,383.33704f),
            new TutorialPoint(283.1428f,399.42932f),
            new TutorialPoint(281.59528f,419.2613f),
            new TutorialPoint(279.14835f,437.18402f),
            new TutorialPoint(276.08435f,455.49384f),
            new TutorialPoint(272.03278f,480.8252f),
            new TutorialPoint(267.36374f,503.09045f),
            new TutorialPoint(262.80295f,520.7225f),
            new TutorialPoint(256.4947f,539.5159f),
            new TutorialPoint(249.25914f,556.6299f),
            new TutorialPoint(243.21567f,568.8726f),
            new TutorialPoint(238.33406f,575.88794f),
            new TutorialPoint(233.72942f,581.27057f),
            new TutorialPoint(229.09332f,584.60443f),
            new TutorialPoint(226.34372f,585.8281f),
            new TutorialPoint(223.24104f,587.37946f),
            new TutorialPoint(220.18669f,588.9067f),
            new TutorialPoint(218.54625f,589.0f),
            new TutorialPoint(217.01903f,589.0f),
            new TutorialPoint(215.47815f,589.52185f),
            new TutorialPoint(213.94473f,591.05524f),
            new TutorialPoint(212.40599f,592.594f),
            new TutorialPoint(210.87485f,594.1252f),
            new TutorialPoint(208.66788f,598.66425f),
            new TutorialPoint(205.6027f,607.1919f),
            new TutorialPoint(201.53075f,624.81555f),
            new TutorialPoint(198.43645f,643.3813f),
            new TutorialPoint(198.0f,661.4467f),
            new TutorialPoint(198.0f,681.45465f),
            new TutorialPoint(199.87318f,702.47815f),
            new TutorialPoint(202.81152f,718.0576f),
            new TutorialPoint(206.84442f,728.63696f),
            new TutorialPoint(211.40436f,737.8087f),
            new TutorialPoint(217.12672f,745.1584f),
            new TutorialPoint(223.35188f,751.35187f),
            new TutorialPoint(230.65349f,757.52277f),
            new TutorialPoint(239.89046f,761.94525f),
            new TutorialPoint(251.66f,764.2075f),
            new TutorialPoint(265.9562f,761.83014f),
            new TutorialPoint(285.41486f,754.6866f),
            new TutorialPoint(305.3103f,746.1207f),
            new TutorialPoint(322.42798f,738.44183f),
            new TutorialPoint(337.58463f,730.7077f),
            new TutorialPoint(354.41983f,722.99097f),
            new TutorialPoint(376.98483f,713.43506f),
            new TutorialPoint(397.02924f,705.6811f),
            new TutorialPoint(419.3051f,702.9765f),
            new TutorialPoint(434.93292f,703.0f),
            new TutorialPoint(449.11768f,704.1242f),
            new TutorialPoint(462.64233f,707.25385f),
            new TutorialPoint(476.54724f,711.51575f),
            new TutorialPoint(488.8683f,716.0864f),
            new TutorialPoint(499.63937f,719.4684f),
            new TutorialPoint(508.6252f,722.54175f),
            new TutorialPoint(515.2675f,724.3169f),
            new TutorialPoint(521.3747f,725.8437f),
            new TutorialPoint(528.9546f,727.3909f),
            new TutorialPoint(539.46094f,727.0f),
            new TutorialPoint(551.517f,727.0f),
            new TutorialPoint(565.0315f,727.0f),
            new TutorialPoint(578.95374f,727.0f),
            new TutorialPoint(592.6159f,725.9316f),
            new TutorialPoint(607.06714f,724.3933f),
            new TutorialPoint(622.53094f,722.8469f),
            new TutorialPoint(637.1128f,721.3208f),
            new TutorialPoint(649.76416f,721.0f),
            new TutorialPoint(660.2857f,720.2449f),
            new TutorialPoint(671.13995f,718.6943f),
            new TutorialPoint(682.59955f,716.3501f),
            new TutorialPoint(694.97784f,713.25555f),
            new TutorialPoint(705.1468f,708.36566f),
            new TutorialPoint(714.7262f,700.72815f),
            new TutorialPoint(725.8229f,690.1771f),
            new TutorialPoint(738.0f,676.5f),
            new TutorialPoint(753.5078f,649.0408f),
            new TutorialPoint(762.8991f,625.3027f),
            new TutorialPoint(770.6552f,598.6412f),
            new TutorialPoint(772.66394f,573.70465f),
            new TutorialPoint(773.0f,555.77997f),
            new TutorialPoint(773.0f,540.6516f),
            new TutorialPoint(774.2717f,522.7391f),
            new TutorialPoint(775.0f,505.22046f),
            new TutorialPoint(776.35236f,490.82874f),
            new TutorialPoint(777.8887f,479.77917f),
            new TutorialPoint(780.79785f,469.2074f),
            new TutorialPoint(784.87f,457.34674f),
            new TutorialPoint(789.43353f,448.13293f),
            new TutorialPoint(794.0869f,441.8841f),
            new TutorialPoint(798.6809f,436.3191f),
            new TutorialPoint(804.41534f,431.68854f),
            new TutorialPoint(813.4365f,429.3662f),
            new TutorialPoint(826.571f,429.0f),
            new TutorialPoint(838.8794f,431.3941f),
            new TutorialPoint(848.4936f,435.74677f),
            new TutorialPoint(855.8357f,440.3014f),
            new TutorialPoint(862.3154f,443.6577f),
            new TutorialPoint(866.4905f,445.83014f),
            new TutorialPoint(869.7623f,447.38116f),
            new TutorialPoint(872.83875f,448.91937f),
            new TutorialPoint(875.98254f,450.49127f),
            new TutorialPoint(877.9949f,451.0f),
            new TutorialPoint(879.5246f,451.0f),
            new TutorialPoint(881.0236f,451.0f),
            new TutorialPoint(882.58624f,451.0f),
            new TutorialPoint(883.5f,451.0f),
            new TutorialPoint(884.6536f,448.6927f),
            new TutorialPoint(886.1676f,442.162f),
            new TutorialPoint(887.0f,425.09198f),
            new TutorialPoint(885.7394f,406.87274f),
            new TutorialPoint(882.40045f,384.80322f),
            new TutorialPoint(879.33295f,364.66425f),
            new TutorialPoint(878.1337f,352.0697f),
            new TutorialPoint(876.58234f,341.07654f),
            new TutorialPoint(875.02f,331.12f),
            new TutorialPoint(873.5f,322.0f),
            new TutorialPoint(869.8792f,310.57733f),
            new TutorialPoint(868.4245f,303.12256f),
            new TutorialPoint(866.86285f,296.45135f),
            new TutorialPoint(865.344f,292.03192f),
            new TutorialPoint(863.79584f,288.5916f),
            new TutorialPoint(860.76855f,286.51236f),
            new TutorialPoint(853.5749f,283.42996f),
            new TutorialPoint(839.67566f,278.55856f),
            new TutorialPoint(827.16016f,275.29004f),
            new TutorialPoint(813.18713f,275.8681f),
            new TutorialPoint(795.1802f,278.8033f),
            new TutorialPoint(773.47406f,289.74472f),
            new TutorialPoint(750.02734f,305.98175f),
            new TutorialPoint(726.5259f,323.37256f),
            new TutorialPoint(709.2955f,339.70453f),
            new TutorialPoint(692.7444f,358.48395f),
            new TutorialPoint(680.5257f,373.11353f),
            new TutorialPoint(670.8946f,383.1054f),
            new TutorialPoint(664.1017f,392.34753f),
            new TutorialPoint(656.6877f,401.57477f),
            new TutorialPoint(650.83136f,410.753f),
            new TutorialPoint(644.7133f,419.93f),
            new TutorialPoint(639.39044f,426.47943f),
            new TutorialPoint(634.81067f,431.18933f),
            new TutorialPoint(632.11176f,432.9441f),
            new TutorialPoint(630.5128f,433.0f),
            new TutorialPoint(626.9546f,431.9773f),
            new TutorialPoint(623.34515f,424.58716f),
            new TutorialPoint(619.8134f,394.76117f),
            new TutorialPoint(620.3765f,356.02448f),
            new TutorialPoint(618.8257f,335.90845f),
            new TutorialPoint(618.0f,321.7259f),
            new TutorialPoint(618.0f,308.99774f),
            new TutorialPoint(618.0f,298.3952f),
            new TutorialPoint(618.0f,290.36688f),
            new TutorialPoint(618.849f,283.60397f),
            new TutorialPoint(620.39514f,277.4194f),
            new TutorialPoint(621.92395f,273.22824f),
            new TutorialPoint(624.93146f,268.60284f),
            new TutorialPoint(627.99316f,264.01028f),
            new TutorialPoint(631.10675f,258.78653f),
            new TutorialPoint(635.21655f,251.63907f),
            new TutorialPoint(640.84705f,242.30594f),
            new TutorialPoint(645.445f,231.96164f),
            new TutorialPoint(650.0798f,221.14713f),
            new TutorialPoint(654.66223f,211.67554f),
            new TutorialPoint(657.5382f,204.15451f),
            new TutorialPoint(660.5998f,195.20071f),
            new TutorialPoint(661.84436f,186.77823f),
            new TutorialPoint(662.0f,180.50488f),
            new TutorialPoint(662.0f,175.26288f),
            new TutorialPoint(660.55145f,170.65442f),
            new TutorialPoint(659.02026f,166.06079f),
            new TutorialPoint(656.0f,161.5f),
            new TutorialPoint(652.89166f,158.89163f),
            new TutorialPoint(649.24713f,155.83145f),
            new TutorialPoint(644.57513f,152.71674f),
            new TutorialPoint(638.35736f,149.67868f),
            new TutorialPoint(632.16473f,147.7912f),
            new TutorialPoint(626.79663f,146.26553f),
            new TutorialPoint(622.1558f,144.7186f),
            new TutorialPoint(617.578f,144.0f),
            new TutorialPoint(611.6099f,144.0f),
            new TutorialPoint(606.50226f,144.0f),
            new TutorialPoint(600.27734f,145.43066f),
            new TutorialPoint(593.19763f,146.96048f),
            new TutorialPoint(587.1154f,148.47113f),
            new TutorialPoint(580.8796f,150.03009f),
            new TutorialPoint(573.74524f,151.56369f),
            new TutorialPoint(568.69293f,153.10236f),
            new TutorialPoint(564.0193f,154.66022f),
            new TutorialPoint(559.4053f,156.19824f),
            new TutorialPoint(554.7581f,157.74728f),
            new TutorialPoint(550.16016f,160.5599f),
            new TutorialPoint(547.3567f,162.64331f),
            new TutorialPoint(544.2584f,164.37079f),
            new TutorialPoint(541.20074f,165.89963f),
            new TutorialPoint(538.11975f,167.44012f),
            new TutorialPoint(535.05524f,168.97238f),
            new TutorialPoint(532.0f,170.5f),
            new TutorialPoint(528.908f,171.0f),
            new TutorialPoint(526.4371f,171.0f),
            new TutorialPoint(523.7883f,171.0f),
            new TutorialPoint(522.3735f,171.0f),
            new TutorialPoint(520.81506f,171.0f),
            new TutorialPoint(519.3077f,171.0f),
            new TutorialPoint(517.74384f,171.0f),
            new TutorialPoint(516.2085f,171.0f),
            new TutorialPoint(514.70544f,171.0f),
            new TutorialPoint(513.15283f,171.0f),
            new TutorialPoint(511.6133f,171.0f),
            new TutorialPoint(510.81613f,171.0f),
            new TutorialPoint(511.0f,171.0f),
            new TutorialPoint(511.0f,171.0f)

    };

    private TutorialPoint[] mRegularCircle = {
            new TutorialPoint(520.0f,187.0f),
            new TutorialPoint(520.0f,187.0f),
            new TutorialPoint(520.0f,187.0f),
            new TutorialPoint(520.0f,187.0f),
            new TutorialPoint(510.75397f,187.0f),
            new TutorialPoint(496.83957f,188.12894f),
            new TutorialPoint(471.54553f,194.93298f),
            new TutorialPoint(442.33975f,205.34851f),
            new TutorialPoint(419.3307f,217.564f),
            new TutorialPoint(390.55322f,232.86673f),
            new TutorialPoint(364.11072f,249.86139f),
            new TutorialPoint(339.47302f,269.49066f),
            new TutorialPoint(315.9796f,291.15234f),
            new TutorialPoint(294.10657f,312.89343f),
            new TutorialPoint(273.61298f,343.05695f),
            new TutorialPoint(258.09708f,370.82526f),
            new TutorialPoint(241.34631f,428.37085f),
            new TutorialPoint(237.78273f,455.9555f),
            new TutorialPoint(237.63101f,484.98932f),
            new TutorialPoint(240.33337f,509.5003f),
            new TutorialPoint(247.4939f,534.578f),
            new TutorialPoint(256.46243f,563.63104f),
            new TutorialPoint(265.60876f,583.98566f),
            new TutorialPoint(277.5289f,604.10944f),
            new TutorialPoint(294.2958f,623.98596f),
            new TutorialPoint(311.33875f,641.33875f),
            new TutorialPoint(332.70483f,658.05023f),
            new TutorialPoint(364.9919f,675.2339f),
            new TutorialPoint(388.8449f,685.9321f),
            new TutorialPoint(422.7833f,696.67804f),
            new TutorialPoint(456.6619f,701.15106f),
            new TutorialPoint(496.0355f,699.1693f),
            new TutorialPoint(530.536f,693.3571f),
            new TutorialPoint(566.68115f,684.90924f),
            new TutorialPoint(597.02014f,675.73047f),
            new TutorialPoint(629.1946f,661.5292f),
            new TutorialPoint(655.2779f,644.37146f),
            new TutorialPoint(677.52875f,625.977f),
            new TutorialPoint(696.88635f,609.92804f),
            new TutorialPoint(711.1645f,594.0212f),
            new TutorialPoint(723.7005f,575.4493f),
            new TutorialPoint(732.86304f,558.2511f),
            new TutorialPoint(742.0f,540.0f),
            new TutorialPoint(750.33154f,514.93896f),
            new TutorialPoint(754.1703f,494.39307f),
            new TutorialPoint(756.13904f,471.9143f),
            new TutorialPoint(756.0f,444.10736f),
            new TutorialPoint(756.0f,418.69806f),
            new TutorialPoint(753.5289f,394.2309f),
            new TutorialPoint(750.4254f,369.4032f),
            new TutorialPoint(747.38635f,352.12488f),
            new TutorialPoint(742.9364f,335.10016f),
            new TutorialPoint(737.4974f,318.36786f),
            new TutorialPoint(731.29254f,299.87756f),
            new TutorialPoint(723.2454f,285.44177f),
            new TutorialPoint(715.0196f,273.02615f),
            new TutorialPoint(705.8072f,260.74292f),
            new TutorialPoint(695.6029f,248.47058f),
            new TutorialPoint(686.3358f,237.22516f),
            new TutorialPoint(674.21906f,228.14606f),
            new TutorialPoint(657.94653f,218.87991f),
            new TutorialPoint(639.4605f,212.1535f),
            new TutorialPoint(622.18335f,207.23181f),
            new TutorialPoint(598.5382f,202.66342f),
            new TutorialPoint(569.1902f,199.28317f),
            new TutorialPoint(536.3291f,198.1109f),
            new TutorialPoint(508.28528f,195.14282f),
            new TutorialPoint(489.41925f,194.0f),
            new TutorialPoint(477.0f,193.0f),
            new TutorialPoint(477.0f,193.0f)
    };





}
