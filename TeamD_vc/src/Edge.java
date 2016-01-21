public class Edge {
	public int end1;
	public int end2;
	
	public Edge(int e1, int e2){
		this.end1 = e1;
		this.end2 = e2;
	}
	
	public int hashCode(){
		return 7*this.end1+13*this.end2;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof Edge))
			return false;
		if(obj==this)
			return true;
		Edge rhs = (Edge) obj;
		if((this.end1==rhs.end1&&this.end2==rhs.end2)||(this.end1==rhs.end2&&this.end2==rhs.end1))
			return true;
		else
			return false;
	}
}
