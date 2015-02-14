package edu.upenn.cis350.hwk2;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Path;
import java.util.Random;
import android.widget.Toast;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;



/**
 * GameView extends View.
 */
public class GameView extends View {
    protected int mouse_state = 0;

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<Point> start_points = new ArrayList<Point>();
    private ArrayList<Point> end_points = new ArrayList<Point>();
    private ArrayList<Location> loc_points = new ArrayList<Location>();
    private Context context;

    protected int loc_points_state = 0;
    private int clear_count=0;
    private int tmp_short_path_state = 0;

    public GameView(Context context) {
        super(context);
        setBackgroundResource(R.drawable.campus);
        Bundle transporter = ((Activity)getContext()).getIntent().getExtras();
        int spinner_number=4;
        if(transporter != null) {
            spinner_number = Integer.parseInt(transporter.getString("SpinnerValueString"));
        }
        ((Activity)getContext()).getIntent().putExtra("undo_state", "-1");
        ((Activity)getContext()).getIntent().putExtra("clear_state", "-1");
        setLocPoints(spinner_number);
        this.context=context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.campus);
        Bundle transporter = ((Activity)getContext()).getIntent().getExtras();
        int spinner_number=4;
        if(transporter != null) {
            spinner_number = Integer.parseInt(transporter.getString("SpinnerValueString"));
        }
        setLocPoints(spinner_number);
        this.context=context;

    }



    @Override
    protected void onDraw(Canvas canvas) {
        String s_undo = ((Activity)getContext()).getIntent().getStringExtra("undo_state");
        if(Integer.parseInt(s_undo) >= 0) {
            //handle undo
            if(!start_points.isEmpty()) {
                start_points.remove(start_points.size() - 1);
                end_points.remove(end_points.size()-1);
            }
            if(!paths.isEmpty()) {
                if(paths.size() >= loc_points.size()) {
                    clear_count=clear_count+1;
                }
                paths.remove(paths.size() - 1);
            }
            loc_points_state = 0;
            tmp_short_path_state = 0;
            ((Activity)getContext()).getIntent().putExtra("undo_state", "-1");
            ((Activity)getContext()).getIntent().putExtra("clear_state", "-1");
        }
        String s_clear = ((Activity)getContext()).getIntent().getStringExtra("clear_state");
        if(Integer.parseInt(s_clear) >= 0) {
            //handle clear
            ((Activity)getContext()).getIntent().putExtra("undo_state", "-1");
            ((Activity)getContext()).getIntent().putExtra("clear_state", "-1");
            if(!start_points.isEmpty()) {
                start_points.clear();
                end_points.clear();
            }
            if(!paths.isEmpty()) {
                if(paths.size() >= loc_points.size()) {
                    clear_count=clear_count+1;
                }
                paths.clear();
            }
            loc_points_state = 0;
            tmp_short_path_state = 0;
            ((Activity)getContext()).getIntent().putExtra("undo_state", "-1");
            ((Activity)getContext()).getIntent().putExtra("clear_state", "-1");
        }

        Paint p_red = new Paint();
        p_red.setColor(Color.RED);
        p_red.setStrokeWidth(15);
        p_red.setStyle(Paint.Style.STROKE);

        Paint p_yel = new Paint();
        p_yel.setColor(Color.YELLOW);
        p_yel.setStrokeWidth(10);
        p_yel.setStyle(Paint.Style.STROKE);

        Path tmp_path = new Path();
        boolean first = true;
        for(Point point: points) {
            if(first) {
                first = false;
                tmp_path.moveTo(point.x, point.y);
            }
            else {
                tmp_path.lineTo(point.x, point.y);
            }
        }
        if(paths.size() < loc_points.size()) {

            canvas.drawPath(tmp_path, p_yel);
            ArrayList<Path> red_paths = new ArrayList<Path>();
            for(Location red_loc_p : loc_points) {
                red_loc_p.degree = 0;
            }
            for (int i = 0; i < start_points.size(); i++) {
                Path red_path = new Path();
                if (!start_points.isEmpty() && start_points.size() == end_points.size()) {
                    Point start_p = start_points.get(i);
                    Point end_p = end_points.get(i);
                    if (nearLocation(start_p) && nearLocation(end_p)) {
                        red_path.moveTo(start_p.x, start_p.y);
                        red_path.lineTo(end_p.x, end_p.y);
                        incrementNearLocation(start_p);
                        incrementNearLocation(end_p);

                        red_paths.add(red_path);
                        paths = red_paths;
                    } else {
                        start_points.remove(i);
                        end_points.remove(i);
                    }
                }
            }
        }

        if(paths.size() >= loc_points.size()) {
            Toast t;
            CharSequence text = "";
            loc_points_state = 1;
            int duration = Toast.LENGTH_SHORT;
            if (!start_points.isEmpty() && start_points.size() == end_points.size()){
                //confirm that all vertices have a Hamiltonian cycle w/ degree 2
                boolean check_degree = true;
                for (Location loc_p : loc_points) {
                    if (loc_p.degree != 2) {
                        check_degree = false;
                    }
                }
                if (!check_degree) {
                    text = "This is not a circuit! Please press Clear to try again.";
                    duration = Toast.LENGTH_SHORT;
                    t = Toast.makeText(context, text, duration);
                    t.show();
                } else {
                    Point[] PointArray = new Point[loc_points.size()];
                    for (int point_index = 0; point_index < PointArray.length; point_index++) {
                        PointArray[point_index] = loc_points.get(point_index).p;
                    }
                    ArrayList<Point> ShortPath = new ShortestPath().shortestPath(PointArray);
                    double total_short_dist = 0;
                    for (int i = 0; i < ShortPath.size(); i++) {
                        double dx = ShortPath.get(i % ShortPath.size()).x
                                - ShortPath.get((i + 1) % ShortPath.size()).x;
                        double dy = ShortPath.get(i % ShortPath.size()).y
                                - ShortPath.get((i + 1) % ShortPath.size()).y;
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        total_short_dist += dist;
                    }
                    double total_path_dist = 0;
                    for (int i = 0; i < start_points.size(); i++) {
                        Point path_start_p = start_points.get(i);
                        Point path_end_p = end_points.get(i);
                        double path_dx = path_start_p.x - path_end_p.x;
                        double path_dy = path_start_p.y - path_end_p.y;
                        double path_dist = Math.sqrt(path_dx * path_dx + path_dy * path_dy);
                        total_path_dist += path_dist;
                    }
                    double off_percentage = (total_path_dist - total_short_dist) / (total_short_dist);
                    if (off_percentage <= 0.01) {
                        text = "You found the shortest path!";
                        duration = Toast.LENGTH_SHORT;
                        t = Toast.makeText(context, text, duration);
                        t.show();
                    } else if (off_percentage > 0.01) {
                        if(clear_count < 2) {
                            text = "Nope, not quite! Your path is about "
                                    + (int) (100 * off_percentage) + " percent too long.";
                            duration = Toast.LENGTH_SHORT;
                            t = Toast.makeText(context, text, duration);
                            t.show();
                        }
                        else if(clear_count >= 2) {
                            //draw path
                            boolean path_first = true;
                            Path tmp_short_path = new Path();
                            for (int i = 0; i < ShortPath.size(); i++) {
                                if(path_first) {
                                    path_first = false;
                                    tmp_short_path.moveTo(ShortPath.get(i).x, ShortPath.get(i).y);
                                }
                                else {
                                    tmp_short_path.lineTo(ShortPath.get(i).x, ShortPath.get(i).y);
                                }
                            }
                            tmp_short_path.close();
                            canvas.drawPath(tmp_short_path, p_yel);

                            if(tmp_short_path_state == 0) {
                                text = "Nope, here's the solution!";
                                duration = Toast.LENGTH_SHORT;
                                t = Toast.makeText(context, text, duration);
                                t.show();
                                tmp_short_path_state++;
                            }

                            tmp_short_path.reset();
                            postInvalidate();
                        }
                    }


                }
            //end of if
            }

        }

        for (Path path : paths) {
            if (!paths.isEmpty()) {
                canvas.drawPath(path, p_red);
            }
        }
        for(Location loc_p : loc_points) {
            int x = (int)loc_p.p.x;
            int y = (int)loc_p.p.y;
            canvas.drawLine(x,y, x+30, y, p_red);
        }
    }

    @Override
     public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() != MotionEvent.ACTION_UP){
            if(mouse_state == 0) {
                mouse_state = 1;
            }
            Point point = new Point();
            point.x = (int)e.getX();
            point.y = (int)e.getY();
            points.add(point);
            if(start_points.size() == end_points.size()) {
                if(loc_points_state != 1) {
                    start_points.add(point);
                }
            }

            invalidate();
            return true;
           }

        if(e.getAction() == MotionEvent.ACTION_UP) {
            if(mouse_state == 1) {
                mouse_state = 0;
            }
            Point end_point = new Point();
            end_point.x = (int)e.getX();
            end_point.y = (int)e.getY();
            if(loc_points_state != 1) {
                end_points.add(end_point);
            }
            points.clear();
            invalidate();
        }

        return super.onTouchEvent(e);
     }

    public void setLocPoints(int spinner_number) {
        //13 points to choose from
        ArrayList<Location> tmp_loc_points = new ArrayList<Location>();
        loc_points.clear();

        Point arch = new Point(140, 240);tmp_loc_points.add(new Location(arch));
        Point vanpelt = new Point(460, 130);tmp_loc_points.add(new Location(vanpelt));
        Point collegehall = new Point(550, 480);tmp_loc_points.add(new Location(collegehall));
        Point houston = new Point(510, 620);tmp_loc_points.add(new Location(houston));
        Point cohen = new Point(260, 520);tmp_loc_points.add(new Location(cohen));
        Point meyerson = new Point(880, 230);tmp_loc_points.add(new Location(meyerson));
        Point drl = new Point(1700, 260);tmp_loc_points.add(new Location(drl));
        Point moore = new Point(1450, 230);tmp_loc_points.add(new Location(moore));
        Point hill = new Point(1360, 80);tmp_loc_points.add(new Location(hill));
        Point town = new Point(1330, 380);tmp_loc_points.add(new Location(town));
        Point fisher = new Point(850, 430);tmp_loc_points.add(new Location(fisher));
        Point morgan = new Point(1040, 370);tmp_loc_points.add(new Location(morgan));
        Point irvine = new Point(790, 650);tmp_loc_points.add(new Location(irvine));

        while(loc_points.size() < spinner_number) {
            int rand_index = new Random().nextInt(13);
            if(!loc_points.contains(tmp_loc_points.get(rand_index))) {
                loc_points.add(tmp_loc_points.get(rand_index));
            }
        }
    }

    public boolean nearLocation(Point p) {
        int xc = (int)p.x;
        int yc = (int)p.y;

        for(Location loc_p: loc_points) {
            int loc_x = (int)loc_p.p.x;
            int loc_y = (int)loc_p.p.y;

            int loc_x_diff = loc_x - xc;
            int loc_y_diff = loc_y - yc;

            if(loc_x_diff*loc_x_diff + loc_y_diff*loc_y_diff <= 625) {
                //change the point coordinates to the location coordinates
                //+10 for alignment
                p.x = loc_x+10;
                p.y = loc_y+10;
                //increment degree if start/end point evaluates to loc_point
                return true;
            }
        }
        return false;
    }

    //valid only if already near location
    public void incrementNearLocation(Point p) {
        int xc = (int)p.x;
        int yc = (int)p.y;

        for(Location loc_p: loc_points) {
            int loc_x = (int)loc_p.p.x;
            int loc_y = (int)loc_p.p.y;

            int loc_x_diff = loc_x - xc;
            int loc_y_diff = loc_y - yc;

            if(loc_x_diff*loc_x_diff + loc_y_diff*loc_y_diff <= 625) {
                //change the point coordinates to the location coordinates
                //+10 for alignment
                p.x = loc_x+10;
                p.y = loc_y+10;
                //increment degree if start/end point evaluates to loc_point
                loc_p.degree++;
                break;
            }
        }
    }
}

class Location {
    Point p;
    int degree;
    Location(Point p) {
        this.p = p;
    }
}

/**
 * This is a bruteforce solution to the Traveling Salesman Problem.
 *
 */
class ShortestPath {

    /*
     * The input is an array of all the Points in the graph.
     * This method then calculates the distance of the circuit through
     * all permutations of the Points, and then returns an ArrayList
     * containing the Points in their order in the shortest path.
     */
    public static ArrayList<Point> shortestPath(Point[] points) {

        // get the permutations of the indices
        ArrayList<ArrayList<Integer>> indices = permute(points.length);

        double shortest = Double.MAX_VALUE;
        ArrayList<Integer> shortestPathIndices = null;

        for (ArrayList<Integer> p : indices) {
            double total = 0;

            // get the distance between the intermediate points
            for (int i = 0; i < p.size()-1; i++) {
                Point p1 = points[p.get(i)];
                Point p2 = points[p.get(i+1)];
                double dx = p1.x - p2.x;
                double dy = p1.y - p2.y;
                double dist = Math.sqrt(dx*dx + dy*dy);
                total += dist;
            }

            // then need to go back to the beginning
            Point p1 = points[p.get(p.size()-1)];
            Point p2 = points[p.get(0)];
            double dist = dist(p1, p2);
            total += dist;

            // see if this is the shortest  so far
            if (total < shortest) {
                shortest = total;
                shortestPathIndices = p;
            }
        }

        // reconstruct the shortest path using the indices
        ArrayList<Point> shortestPath = new ArrayList<Point>();
        for (int k : shortestPathIndices)
            shortestPath.add(points[k]);

        return shortestPath;
    }

    /*
     * from http://www.programcreek.com/2013/02/leetcode-permutations-java/
     */
    private static ArrayList<ArrayList<Integer>> permute(int vals) {

        int[] num = new int[vals];
        for (int i = 0; i < vals; i++) num[i] = i;

        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

        //start from an empty list
        result.add(new ArrayList<Integer>());

        for (int i = 0; i < num.length; i++) {
            //list of list in current iteration of the array num
            ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();

            for (ArrayList<Integer> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size()+1; j++) {
                    // + add num[i] to different locations
                    l.add(j, num[i]);

                    ArrayList<Integer> temp = new ArrayList<Integer>(l);

                    current.add(temp);

                    // - remove num[i] add
                    l.remove(j);
                    System.gc();
                }
            }
            result.clear();
            for (ArrayList<Integer> k : current) {
                result.add(k);
            }
        }

        return result;
    }

    public static double dist(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
    }
}

