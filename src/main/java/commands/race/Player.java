package commands.race;

public class Player {
	public static String playerimg = ":construction_worker:";
	private static final Integer WIN_STATE = 10;
	private static final Integer FAIL_STATE = 0;
	
	private int player;
	private int status;
	private int failstate;
	
	public Player(int player) {
		player = this.player;
		status = 0;		//Si llega a 10 es que ha sobrevivido.
		failstate = 10; //Si llega a 0 es que ha muerto.
	}
	
	/**
	 *  Avanza en la carrera dependiendo de la suerte
	 */
	public void advance() {
		 
		 int numAleatorio = (int) Math.floor(Math.random()*(6-1+1)+(1)); //Da enteros aleatorios.
		 
		 if(numAleatorio == 4) { //Éxito.
			 
			 status++;
			 
		 } else if(numAleatorio > 4) { //Más éxito.
			 
			 if(numAleatorio == 5) {
				 
				 status = status + 2; 
				 
			 } else if(numAleatorio == 6) {
				 
				 status = status + 3;
				 
			 } 
			 
		 } else if(numAleatorio < 4) { //Tas colao.
			 
			 if(numAleatorio == 3) {
				 
				 failstate--;
				 
			 } else if(numAleatorio == 2) {
				 
				 failstate = failstate - 2;
				 
			 } else if(numAleatorio == 1) {
				 
				 failstate = failstate - 3;
				 
			 }
		 }
		 
	}
	
	public boolean winstate() {
		return status==WIN_STATE; //Ganar
	}
	
	public boolean failstate() {
		return failstate==FAIL_STATE; //Perder
	}
	
	
}
