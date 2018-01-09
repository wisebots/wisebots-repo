package com.wisebots.dataset;

public class BotGenesis {
	
	private String complexity;
	private int statesize;
	private int maxact;
	private long maxtimepacts;
	private long medtimeacts;
	private long maxtimemakeacts;
	private long medtimemakeacts;
	private String probsmax;
	private String axisx;
	private String axisy;
	
	private String statusMeme;
	private String finalStatusMeme;
	private String statusMemory;
	private String finalStatusMemory;
	private String lastcheck;
	private String creationDate;
	private String finishedDate;
	private String finishedMemoryDate;
	private String imagestatus;
	
	public String getComplexity() {
		return complexity;
	}
	
	

	public int getStatesize() {
		return statesize;
	}



	public void setStatesize(int statesize) {
		this.statesize = statesize;
	}



	public int getMaxact() {
		return maxact;
	}



	public void setMaxact(int maxact) {
		this.maxact = maxact;
	}



	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

	public long getMaxtimepacts() {
		return maxtimepacts;
	}

	public void setMaxtimepacts(long maxtimepacts) {
		this.maxtimepacts = maxtimepacts;
	}

	public long getMedtimeacts() {
		return medtimeacts;
	}

	public void setMedtimeacts(long medtimeacts) {
		this.medtimeacts = medtimeacts;
	}

	public long getMaxtimemakeacts() {
		return maxtimemakeacts;
	}

	public void setMaxtimemakeacts(long maxtimemakeacts) {
		this.maxtimemakeacts = maxtimemakeacts;
	}

	public long getMedtimemakeacts() {
		return medtimemakeacts;
	}

	public void setMedtimemakeacts(long medtimemakeacts) {
		this.medtimemakeacts = medtimemakeacts;
	}

	public String getProbsmax() {
		return probsmax;
	}

	public void setProbsmax(String probsmax) {
		this.probsmax = probsmax;
	}



	public String getStatusMeme() {
		return statusMeme;
	}



	public void setStatusMeme(String statusMeme) {
		this.statusMeme = statusMeme;
	}



	public String getFinalStatusMeme() {
		return finalStatusMeme;
	}



	public void setFinalStatusMeme(String finalStatusMeme) {
		this.finalStatusMeme = finalStatusMeme;
	}



	public String getStatusMemory() {
		return statusMemory;
	}



	public void setStatusMemory(String statusMemory) {
		this.statusMemory = statusMemory;
	}



	public String getFinalStatusMemory() {
		return finalStatusMemory;
	}



	public void setFinalStatusMemory(String finalStatusMemory) {
		this.finalStatusMemory = finalStatusMemory;
	}

	


	public String getLastcheck() {
		return lastcheck;
	}



	public void setLastcheck(String lastcheck) {
		this.lastcheck = lastcheck;
	}



	public String getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}



	public String getFinishedDate() {
		return finishedDate;
	}



	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}



	public String getFinishedMemoryDate() {
		return finishedMemoryDate;
	}



	public void setFinishedMemoryDate(String finishedMemoryDate) {
		this.finishedMemoryDate = finishedMemoryDate;
	}

	


	public String getAxisx() {
		return axisx;
	}



	public void setAxisx(String axisx) {
		this.axisx = axisx;
	}



	public String getAxisy() {
		return axisy;
	}



	public void setAxisy(String axisy) {
		this.axisy = axisy;
	}

	


	public String getImagestatus() {
		return imagestatus;
	}



	public void setImagestatus(String imagestatus) {
		this.imagestatus = imagestatus;
	}



	@Override
	public String toString() {
		return "BotGenesis [complexity=" + complexity + ", statesize="
				+ statesize + ", maxact=" + maxact + ", maxtimepacts="
				+ maxtimepacts + ", medtimeacts=" + medtimeacts
				+ ", maxtimemakeacts=" + maxtimemakeacts + ", medtimemakeacts="
				+ medtimemakeacts + ", probsmax=" + probsmax + ", statusMeme="
				+ statusMeme + ", finalStatusMeme=" + finalStatusMeme
				+ ", statusMemory=" + statusMemory + ", finalStatusMemory="
				+ finalStatusMemory + ", lastcheck=" + lastcheck
				+ ", creationDate=" + creationDate + ", finishedDate="
				+ finishedDate + ", finishedMemoryDate=" + finishedMemoryDate
				+ "]";
	}



}
