import org.joml.*;
import java.lang.Math;
import java.awt.Color;

public class Planet {
	protected Vector3f position;
	protected Vector3f velocity;
	protected float mass;
	protected int r;
	protected Color color;

	public Planet(Planet a) {
		this.position = new Vector3f(a.getPosition());
		this.velocity = new Vector3f(a.getVelocity());
		this.mass = a.getMass();
		this.color = a.getColor();
		this.r = a.r;

	}

	public Planet(Vector3f position, Vector3f velocity, float mass) {
		this.position = position;
		this.velocity = velocity;
		this.mass = mass;
		this.r = (int) Math.sqrt(mass / (Math.pow(10, 22)));
		// this.r=(int) Math.pow(mass/(Math.pow(10, 22)), 1/3);
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public float getMass() {
		return mass;
	}

	public float getR() {
		return r;
	}

	public Color getColor() {
		return color;
	}

	public void setColor() {
		this.color = (new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
	}

	public void setColor(int x, int y, int z) {
		this.color = (new Color(x, y, z));
	}

	public void increaseVelocity(Vector3f a) {
		this.velocity.x += a.x;
		this.velocity.y += a.y;
		this.velocity.z += a.z;
	}

	public void increasePosition(Vector3f a) {
		this.position.x += a.x;
		this.position.y += a.y;
		this.position.z += a.z;
	}
	
	public void setPosition(Vector3f a) {
		this.position.x = a.x;
		this.position.y = a.y;
		this.position.z = a.z;
	}
	
	public void increasePosition(float a, float b, float c) {
		this.position.x += a;
		this.position.y += b;
		this.position.z += c;
	}
	
	public void setLagrangian(int tier,Planet first, Planet second) {
		float r=(float) Math.sqrt((Math.pow(first.getPosition().x-second.getPosition().x, 2)+Math.pow(second.getPosition().y-first.getPosition().y, 2)));
		float sinAlpha= (first.getPosition().x-second.getPosition().x)/r;
		float cosAlpha= (second.getPosition().y-first.getPosition().y)/r;
		float d, d2;
		if (tier==0){
			Vector3f tmp=new Vector3f(second.getPosition());
			d=(float) (r*Math.cbrt(second.getMass()/(3*first.getMass())));
			tmp.add(-d*sinAlpha,d*cosAlpha,0);
			setPosition(tmp);
		}
		else if (tier==1){
			Vector3f tmp=new Vector3f(second.getPosition());
			d=(float) (r*Math.cbrt(second.getMass()/(3*first.getMass())));
			tmp.add(d*sinAlpha,-d*cosAlpha,0);
			setPosition(tmp);
		}
		else if (tier==2){
			Vector3f tmp=new Vector3f(first.getPosition());
			d=r+r*5*second.getMass()/(12*first.getMass());
			tmp.add(d*sinAlpha,-d*cosAlpha,0);
			setPosition(tmp);
		}
		else if (tier==3){
			Vector3f tmp=new Vector3f(first.getPosition());
			d=(first.getMass()-second.getMass())*r/(2*(first.getMass()+second.getMass()));
			d2=(float) Math.sqrt(3)*r/2;
			tmp.add(d*sinAlpha,-d*cosAlpha,0);
			tmp.add(d2*cosAlpha,d2*sinAlpha,0);
			setPosition(tmp);
		}
		else if (tier==4){
			Vector3f tmp=new Vector3f(first.getPosition());
			d=(first.getMass()-second.getMass())*r/(2*(first.getMass()+second.getMass()));
			d2=(float) Math.sqrt(3)*r/2;
			tmp.add(d*sinAlpha,-d*cosAlpha,0);
			tmp.add(-d2*cosAlpha,-d2*sinAlpha,0);
			setPosition(tmp);
		}
		else{}
	}



}
