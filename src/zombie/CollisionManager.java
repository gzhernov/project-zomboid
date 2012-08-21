// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.08.2012 13:54:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CollisionManager.java

package zombie;

import java.util.*;
import zombie.characters.IsoSurvivor;
import zombie.core.Collections.NulledArrayList;
import zombie.core.collision.Polygon;
import zombie.iso.IsoCell;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoPushableObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;

public class CollisionManager
{
    public class Contact
    {

        public IsoMovingObject a;
        public IsoMovingObject b;
       

        public Contact(IsoMovingObject a, IsoMovingObject b)
        {
           
            this.a = a;
            this.b = b;
        }
    }

    public class PolygonCollisionResult
    {

        public boolean WillIntersect;
        public boolean Intersect;
        public Vector2 MinimumTranslationVector;
       

        public PolygonCollisionResult()
        {
           
            MinimumTranslationVector = new Vector2();
        }
    }


    public CollisionManager()
    {
        minA = 0.0F;
        minB = 0.0F;
        maxA = 0.0F;
        maxB = 0.0F;
        result = new PolygonCollisionResult();
        ContactMap = new NulledArrayList();
        longArray = new Long[1000];
        contacts = new Stack();
        vel = new Vector2();
        vel2 = new Vector2();
    }

    private void ProjectPolygonA(Vector2 axis, Polygon polygon)
    {
        float d = axis.dot((Vector2)polygon.points.get(0));
        minA = d;
        maxA = d;
        for(int i = 0; i < polygon.points.size(); i++)
        {
            d = ((Vector2)polygon.points.get(i)).dot(axis);
            if(d < minA)
            {
                minA = d;
                continue;
            }
            if(d > maxA)
                maxA = d;
        }

    }

    private void ProjectPolygonB(Vector2 axis, Polygon polygon)
    {
        float d = axis.dot((Vector2)polygon.points.get(0));
        minB = d;
        maxB = d;
        for(int i = 0; i < polygon.points.size(); i++)
        {
            d = ((Vector2)polygon.points.get(i)).dot(axis);
            if(d < minB)
            {
                minB = d;
                continue;
            }
            if(d > maxB)
                maxB = d;
        }

    }

    public PolygonCollisionResult PolygonCollision(Vector2 velocity)
    {
        result.Intersect = true;
        result.WillIntersect = true;
        result.MinimumTranslationVector.x = 0.0F;
        result.MinimumTranslationVector.y = 0.0F;
        int edgeCountA = polygonA.edges.size();
        int edgeCountB = polygonB.edges.size();
        float minIntervalDistance = (1.0F / 0.0F);
        Vector2 translationAxis = new Vector2();
        for(int edgeIndex = 0; edgeIndex < edgeCountA + edgeCountB; edgeIndex++)
        {
            Vector2 edge;
            if(edgeIndex < edgeCountA)
                edge = (Vector2)polygonA.edges.get(edgeIndex);
            else
                edge = (Vector2)polygonB.edges.get(edgeIndex - edgeCountA);
            axis.x = -edge.y;
            axis.y = edge.x;
            axis.normalize();
            minA = 0.0F;
            minB = 0.0F;
            maxA = 0.0F;
            maxB = 0.0F;
            ProjectPolygonA(axis, polygonA);
            ProjectPolygonB(axis, polygonB);
            if(IntervalDistance(minA, maxA, minB, maxB) > 0.0F)
                result.Intersect = false;
            float velocityProjection = axis.dot(velocity);
            if(velocityProjection < 0.0F)
                minA += velocityProjection;
            else
                maxA += velocityProjection;
            float intervalDistance = IntervalDistance(minA, maxA, minB, maxB);
            if(intervalDistance > 0.0F)
                result.WillIntersect = false;
            if(!result.Intersect && !result.WillIntersect)
                break;
            intervalDistance = Math.abs(intervalDistance);
            if(intervalDistance >= minIntervalDistance)
                continue;
            minIntervalDistance = intervalDistance;
            translationAxis.x = axis.x;
            translationAxis.y = axis.y;
            temp.x = polygonA.Center().x - polygonB.Center().x;
            temp.y = polygonA.Center().y - polygonB.Center().y;
            if(temp.dot(translationAxis) < 0.0F)
            {
                translationAxis.x = -translationAxis.x;
                translationAxis.y = -translationAxis.y;
            }
        }

        if(result.WillIntersect)
        {
            result.MinimumTranslationVector.x = translationAxis.x * minIntervalDistance;
            result.MinimumTranslationVector.y = translationAxis.y * minIntervalDistance;
        }
        return result;
    }

    public float IntervalDistance(float minA, float maxA, float minB, float maxB)
    {
        if(minA < minB)
            return minB - maxA;
        else
            return minA - maxB;
    }

    public void initUpdate()
    {
        if(longArray[0] == null)
        {
            for(int n = 0; n < longArray.length; n++)
                longArray[n] = new Long(0L);

        }
        for(int n = 0; n < ContactMap.size(); n++)
        {
            ((Contact)ContactMap.get(n)).a = null;
            ((Contact)ContactMap.get(n)).b = null;
            contacts.push(ContactMap.get(n));
        }

        ContactMap.clear();
    }

    public void AddContact(IsoMovingObject a, IsoMovingObject b)
    {
        if(a.getID() < b.getID())
            ContactMap.add(contact(a, b));
    }

    Contact contact(IsoMovingObject a, IsoMovingObject b)
    {
        if(contacts.isEmpty())
        {
            for(int n = 0; n < 50; n++)
                contacts.push(new Contact(null, null));

        }
        Contact c = (Contact)contacts.pop();
        c.a = a;
        c.b = b;
        return c;
    }

    public void ResolveContacts()
    {
        Iterator it2 = IsoWorld.instance.CurrentCell.getPushableObjectList().iterator();
        do
        {
            if(it2 == null || !it2.hasNext())
                break;
            IsoPushableObject o = (IsoPushableObject)it2.next();
            if(o.getImpulsex() != 0.0F || o.getImpulsey() != 0.0F)
                if(o.connectList != null)
                {
                    pushables.add(o);
                } else
                {
                    o.setNx(o.getNx() + o.getImpulsex());
                    o.setNy(o.getNy() + o.getImpulsey());
                    o.setImpulsex(o.getNx() - o.getX());
                    o.setImpulsey(o.getNy() - o.getY());
                    o.setNx(o.getX());
                    o.setNy(o.getY());
                }
        } while(true);
        for(int n = 0; n < pushables.size(); n++)
        {
            IsoPushableObject p = (IsoPushableObject)pushables.get(n);
            float impulseTotx = 0.0F;
            float impulseToty = 0.0F;
            for(int m = 0; m < p.connectList.size(); m++)
            {
                impulseTotx += ((IsoPushableObject)p.connectList.get(m)).getImpulsex();
                impulseToty += ((IsoPushableObject)p.connectList.get(m)).getImpulsey();
            }

            impulseTotx /= p.connectList.size();
            impulseToty /= p.connectList.size();
            for(int m = 0; m < p.connectList.size(); m++)
            {
                ((IsoPushableObject)p.connectList.get(m)).setImpulsex(impulseTotx);
                ((IsoPushableObject)p.connectList.get(m)).setImpulsey(impulseToty);
                int inof = pushables.indexOf(p.connectList.get(m));
                pushables.remove(p.connectList.get(m));
                if(inof <= n)
                    n--;
            }

            if(n < 0)
                n = 0;
        }

        pushables.clear();
        if(!ContactMap.isEmpty())
        {
            Iterator it = ContactMap.iterator();
            do
            {
                if(it == null || !it.hasNext())
                    break;
                Contact c = (Contact)it.next();
                if(Math.abs(c.a.getZ() - c.b.getZ()) <= 0.3F)
                {
                    vel.x = c.a.getNx() - c.a.getX();
                    vel.y = c.a.getNy() - c.a.getY();
                    vel2.x = c.b.getNx() - c.b.getX();
                    vel2.y = c.b.getNy() - c.b.getY();
                    if(vel.x != 0.0F || vel.y != 0.0F || vel2.x != 0.0F || vel2.y != 0.0F || c.a.getImpulsex() != 0.0F || c.a.getImpulsey() != 0.0F || c.b.getImpulsex() != 0.0F || c.b.getImpulsey() != 0.0F)
                    {
                        float ax1 = c.a.getX() - c.a.getWidth();
                        float ax2 = c.a.getX() + c.a.getWidth();
                        float ay1 = c.a.getY() - c.a.getWidth();
                        float ay2 = c.a.getY() + c.a.getWidth();
                        float bx1 = c.b.getX() - c.b.getWidth();
                        float bx2 = c.b.getX() + c.b.getWidth();
                        float by1 = c.b.getY() - c.b.getWidth();
                        float by2 = c.b.getY() + c.b.getWidth();
                        polygonA.Set(ax1, ay1, ax2, ay2);
                        polygonB.Set(bx1, by1, bx2, by2);
                        PolygonCollisionResult result = PolygonCollision(vel);
                        if(result.WillIntersect)
                        {
                            c.a.collideWith(c.b);
                            c.b.collideWith(c.a);
                            float weightdelta = 1.0F - c.a.getWeight(result.MinimumTranslationVector.x, result.MinimumTranslationVector.y) / (c.a.getWeight(result.MinimumTranslationVector.x, result.MinimumTranslationVector.y) + c.b.getWeight(result.MinimumTranslationVector.x, result.MinimumTranslationVector.y));
                            if((c.a instanceof IsoPushableObject) && (c.b instanceof IsoSurvivor))
                            {
                                ((IsoSurvivor)c.b).bCollidedWithPushable = true;
                                ((IsoSurvivor)c.b).collidePushable = (IsoPushableObject)c.a;
                            } else
                            if((c.b instanceof IsoPushableObject) && (c.a instanceof IsoSurvivor))
                            {
                                ((IsoSurvivor)c.a).bCollidedWithPushable = true;
                                ((IsoSurvivor)c.a).collidePushable = (IsoPushableObject)c.b;
                            }
                            if((c.a instanceof IsoPushableObject) && ((IsoPushableObject)c.a).connectList != null)
                            {
                                IsoPushableObject p;
                                for(Iterator it3 = ((IsoPushableObject)c.a).connectList.iterator(); it3.hasNext(); p.setImpulsey(p.getImpulsey() + result.MinimumTranslationVector.y * weightdelta))
                                {
                                    p = (IsoPushableObject)it3.next();
                                    p.setImpulsex(p.getImpulsex() + result.MinimumTranslationVector.x * weightdelta);
                                }

                            } else
                            {
                                c.a.setImpulsex(c.a.getImpulsex() + result.MinimumTranslationVector.x * weightdelta);
                                c.a.setImpulsey(c.a.getImpulsey() + result.MinimumTranslationVector.y * weightdelta);
                            }
                            if((c.b instanceof IsoPushableObject) && ((IsoPushableObject)c.b).connectList != null)
                            {
                                Iterator it3 = ((IsoPushableObject)c.b).connectList.iterator();
                                while(it3.hasNext()) 
                                {
                                    IsoPushableObject p = (IsoPushableObject)it3.next();
                                    p.setImpulsex(p.getImpulsex() - result.MinimumTranslationVector.x * (1.0F - weightdelta));
                                    p.setImpulsey(p.getImpulsey() - result.MinimumTranslationVector.y * (1.0F - weightdelta));
                                }
                            } else
                            {
                                c.b.setImpulsex(c.b.getImpulsex() - result.MinimumTranslationVector.x * (1.0F - weightdelta));
                                c.b.setImpulsey(c.b.getImpulsey() - result.MinimumTranslationVector.y * (1.0F - weightdelta));
                            }
                        }
                    }
                }
            } while(true);
        }
        IsoMovingObject o;
        for(Iterator it22 = IsoWorld.instance.CurrentCell.getObjectList().iterator(); it22 != null && it22.hasNext(); o.postupdate())
            o = (IsoMovingObject)it22.next();

    }

    static Vector2 temp = new Vector2();
    static Vector2 axis = new Vector2();
    static Polygon polygonA = new Polygon();
    static Polygon polygonB = new Polygon();
    float minA;
    float minB;
    float maxA;
    float maxB;
    PolygonCollisionResult result;
    public NulledArrayList ContactMap;
    Long longArray[];
    Stack contacts;
    Vector2 vel;
    Vector2 vel2;
    static NulledArrayList pushables = new NulledArrayList();
    public static CollisionManager instance = new CollisionManager();

}