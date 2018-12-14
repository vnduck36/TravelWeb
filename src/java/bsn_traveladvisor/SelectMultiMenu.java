/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsn_traveladvisor;

/**
 *
 * @author leventleger
 */
public class SelectMultiMenu 
{
        private String key;
	private String value;

	public SelectMultiMenu(String key, String value) 
        {
		this.key = key;
		this.value = value;
	}

        public String getKey() {
            return key;
        }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
