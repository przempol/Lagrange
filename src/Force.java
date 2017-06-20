import java.util.List;
import org.joml.Vector3f;

public class Force {

	final float G = (float) -6.67E-11;
	Vector3f[] k1v; // first increment for every
	Vector3f[] k1r;		// variable
	Vector3f[] k2r; // second increment for every
	Vector3f[] k2v;		// variable
	Vector3f[] k3r; // third increment for every
	Vector3f[] k3v;			// variable
	Vector3f[] k4r; // fourth increment for every
	Vector3f[] k4v;					// variable
	private boolean isRunning = true;
	Vector3f[] position;
	Vector3f[] velocity;
	List<Planet> planets;
	int currentPlanet;
	
	public void changeStatement(){
		if (isRunning==true) isRunning=false;
		else if (isRunning==false) isRunning=true;
	}
	
	
	public void setDimension(int d) {
		k1r = new Vector3f[d];
		k2r = new Vector3f[d];
		k3r = new Vector3f[d];
		k4r = new Vector3f[d];
		k1v = new Vector3f[d];
		k2v = new Vector3f[d];
		k3v = new Vector3f[d];
		k4v = new Vector3f[d];
		position= new Vector3f[d];
		velocity= new Vector3f[d];
		for (int ii = 0; ii < d; ii++) {
			position[0] = new Vector3f(0f, 0f ,0f);
			velocity[0] = new Vector3f(0f, 0f ,0f);
			k1r[0] = new Vector3f(0f, 0f ,0f);
			k2r[0] = new Vector3f(0f, 0f ,0f);
			k3r[0] = new Vector3f(0f, 0f ,0f);
			k4r[0] = new Vector3f(0f, 0f ,0f);
			k1v[0] = new Vector3f(0f, 0f ,0f);
			k2v[0] = new Vector3f(0f, 0f ,0f);
			k3v[0] = new Vector3f(0f, 0f ,0f);
			k4v[0] = new Vector3f(0f, 0f ,0f);
		}

	}

	public Force(List<Planet> planets) {
		this.planets = planets;
		setDimension(this.planets.size());
	}

	public Vector3f firstMethod(Planet tmp, float dt) { // from first equation dx/dt=V
		return new Vector3f((tmp.getVelocity().x * dt), (tmp.getVelocity().y * dt), (tmp.getVelocity().z * dt));
	}
	public Vector3f firstMethod(Vector3f tmp, float dt) { // from first equation dx/dt=V
		return new Vector3f((tmp.x * dt), (tmp.y * dt), (tmp.z * dt));
	}
	
	
	public Vector3f secondMethod(Planet tmp, float dt, int tier) { // from second equation dv/dt=G*m*x/r^3
		Vector3f force = new Vector3f(0f, 0f, 0f);
		Vector3f forcetmp = new Vector3f(0f, 0f, 0f);
		float r3Scalar=0;
		float scalar=0;
		
		for (int ii = 0; ii < planets.size(); ii++) {
			if (ii!=currentPlanet){
				forcetmp.add(tmp.getPosition());
				forcetmp.sub(planets.get(ii).getPosition());
				r3Scalar=(float) Math.pow(Math.pow((tmp.getPosition().x-planets.get(ii).getPosition().x),2)+Math.pow((tmp.getPosition().y-planets.get(ii).getPosition().y),2)+Math.pow((tmp.getPosition().z-planets.get(ii).getPosition().z),2),1.5);
				scalar=(float) (G * dt * planets.get(ii).getMass()/r3Scalar);
				forcetmp.mul(scalar);
				force.add(forcetmp);
				forcetmp=new Vector3f(0f, 0f, 0f);
				r3Scalar=0;
				scalar=0;
			}
		}
		
		
		return force;
	}
	public Vector3f secondMethod(Vector3f tmp, float dt, int tier) { // from second equation dv/dt=G*m*x/r^3
		Vector3f force = new Vector3f(0f, 0f, 0f);
		Vector3f forcetmp = new Vector3f(0f, 0f, 0f);
		float r3Scalar=0;
		float scalar=0;
		
		for (int ii = 0; ii < planets.size(); ii++) {
			if (ii!=currentPlanet){
				forcetmp.add(tmp);
				forcetmp.sub(planets.get(ii).getPosition());
				r3Scalar=(float) Math.pow(Math.pow((tmp.x-planets.get(ii).getPosition().x),2)+Math.pow((tmp.y-planets.get(ii).getPosition().y),2)+Math.pow((tmp.z-planets.get(ii).getPosition().z),2),1.5);
				scalar=(float) (G * dt * planets.get(ii).getMass()/r3Scalar);
				forcetmp.mul(scalar);
				force.add(forcetmp);
				forcetmp=new Vector3f(0f, 0f, 0f);
				r3Scalar=0;
				scalar=0;
			}

		}
		
		return force;
	}
	

	void firstUpdate() { // Runge–Kutta method after first step etc 
		for (int ii = 0; ii < this.planets.size(); ii++){
			Vector3f tmp=new Vector3f(k1r[ii]);
			tmp.div(2);
			position[ii].add(tmp);
			
			tmp=new Vector3f(k1v[ii]);
			tmp.div(2);
			velocity[ii].add(tmp);
		}
	}

	void secondUpdate() {
		for (int ii = 0; ii < this.planets.size(); ii++){
			Vector3f tmp=new Vector3f(k1r[ii]);
			tmp.div(2);
			position[ii].sub(tmp);
			tmp=new Vector3f(k2r[ii]);
			tmp.div(2);
			position[ii].add(tmp);
			
			tmp=new Vector3f(k1v[ii]);
			tmp.div(2);
			velocity[ii].sub(tmp);
			tmp=new Vector3f(k2v[ii]);
			tmp.div(2);
			velocity[ii].add(tmp);
		}
	}

	void thirdUpdate() {
		for (int ii = 0; ii < this.planets.size(); ii++){
			Vector3f tmp=new Vector3f(k2r[ii]);
			tmp.div(2);
			position[ii].sub(tmp);
			tmp=new Vector3f(k3r[ii]);
			position[ii].add(tmp);
			
			tmp=new Vector3f(k2v[ii]);
			tmp.div(2);
			velocity[ii].sub(tmp);
			tmp=new Vector3f(k3v[ii]);
			velocity[ii].add(tmp);
		}

	}

	void fourthUpdate() {
		for (int ii = 0; ii < this.planets.size(); ii++){
			Vector3f tmp=new Vector3f(k3r[ii]);
			position[ii].sub(tmp);
			
			tmp=new Vector3f(k3v[ii]);
			velocity[ii].add(tmp);
		}
	}

	void cleanSteps(){
		for (int ii = 0; ii < this.planets.size(); ii++) {
			k1r[ii]=new Vector3f(0f,0f,0f);
			k2r[ii]=new Vector3f(0f,0f,0f);
			k3r[ii]=new Vector3f(0f,0f,0f);
			k4r[ii]=new Vector3f(0f,0f,0f);
			k1v[ii]=new Vector3f(0f,0f,0f);
			k2v[ii]=new Vector3f(0f,0f,0f);
			k3v[ii]=new Vector3f(0f,0f,0f);
			k4v[ii]=new Vector3f(0f,0f,0f);
		}
	}
	
	public synchronized void calculate() { // approximation
	//	float dt = 0.00166f;
		float dt = 3600000f;
		while (isRunning) {
			for (int jj=0; jj<planets.size();jj++){
				currentPlanet=jj;
				position[jj] = new Vector3f(planets.get(jj).getPosition());
				velocity[jj] = new Vector3f(planets.get(jj).getVelocity());
			}
			cleanSteps();
			for (int ii = 0; ii < planets.size(); ii++){
				currentPlanet=ii;
				k1r[ii].add(firstMethod(velocity[ii],dt));
				k1v[ii].add(secondMethod(position[ii],dt,0));
			}
			firstUpdate();
			
			for (int ii = 0; ii < planets.size(); ii++){
				currentPlanet=ii;
				k2r[ii].add(firstMethod(velocity[ii],dt));
				k2v[ii].add(secondMethod(position[ii],dt,1));
				
			}
			secondUpdate();
			
			for (int ii = 0; ii < planets.size(); ii++){
				currentPlanet=ii;
				k3r[ii].add(firstMethod(velocity[ii],dt));
				k3v[ii].add(secondMethod(position[ii],dt,1));
			}
			thirdUpdate();
			
			for (int ii = 0; ii < planets.size(); ii++){
				currentPlanet=ii;
				k4r[ii].add(firstMethod(velocity[ii],dt));
				k4v[ii].add(secondMethod(position[ii],dt,2));
			}
			fourthUpdate();

			for (int ii = 0; ii < planets.size(); ii++){
				currentPlanet=ii;
				Vector3f tmp=new Vector3f(k1r[ii]);
				tmp.add(k2r[ii]);
				tmp.add(k2r[ii]);
				tmp.add(k3r[ii]);
				tmp.add(k3r[ii]);
				tmp.add(k4r[ii]);
				tmp.div(6);
				planets.get(ii).increasePosition(tmp);
				
				tmp=new Vector3f(k1v[ii]);
				tmp.add(k2v[ii]);
				tmp.add(k2v[ii]);
				tmp.add(k3v[ii]);
				tmp.add(k3v[ii]);
				tmp.add(k4v[ii]);
				tmp.div(6);
				planets.get(ii).increaseVelocity(tmp);

			}
			cleanSteps();
			changeStatement();
		}
	}

/*	public void start() {
		isRunning = true;
	}

	public void finish() {
		isRunning = false;
	}
*/


}
