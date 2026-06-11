package logica;

import java.time.LocalDateTime;

public class Alerta {
	private String mensaje;
    private boolean recurrente;
    private int intervalo;
    private boolean activa;
    private LocalDateTime ultimaMostrada;
    
    public Alerta(String mensaje, boolean recurrente, int intervalo) {
    	this.mensaje = mensaje;
    	this.recurrente = recurrente;
    	this.intervalo = intervalo;
        this.activa = true;
        this.ultimaMostrada = null;
    }

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public boolean isRecurrente() {
		return recurrente;
	}

	public int getIntervalo() {
		return intervalo;
	}
    
    public boolean debeActivarse() {
    	if (!activa)
    		return false;
    	else if (ultimaMostrada == null)
    		return true;
    	else if (recurrente) {
    		LocalDateTime ahora = LocalDateTime.now();
    		LocalDateTime segundos  = ultimaMostrada.plusSeconds(intervalo);
    		return ahora.isAfter(segundos);
    	}
    	return false;
    }
    
    public void registrarMostrada() {
    	ultimaMostrada = LocalDateTime.now();
    	if (!recurrente)
    		activa = false;
    }
    
}
