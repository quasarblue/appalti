/*
 * Copyright (C) 2021 Fabrizio Candon

 * This file is part of Direzione.
 
 * Direzione is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Direzione is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Direzione.  If not, see <https://www.gnu.org/licenses/>.
 */
package it.veneto.regione.aagg.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.veneto.regione.aagg.web.AppaltoRepository;
import it.veneto.regione.aagg.web.DipendenteRepository;
import it.veneto.regione.aagg.web.K;
import it.veneto.regione.aagg.web.ProgrammaLavoriRepository;
import it.veneto.regione.aagg.web.ProgrammaServiziRepository;
import it.veneto.regione.aagg.web.model.Appalto;
import it.veneto.regione.aagg.web.model.ProgrammaLavori;
import it.veneto.regione.aagg.web.model.ProgrammaServizi;

@Controller
public class GreetingController {

    // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    @Autowired 
    private DipendenteRepository dipendenteRepository;
    
    @Autowired 
    private AppaltoRepository appaltoRepository;
    
    @Autowired 
    private ProgrammaLavoriRepository programmaLavoriRepository;
    
    @Autowired 
    private ProgrammaServiziRepository programmaServiziRepository;
    
    
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@GetMapping(path="/dipendenti")
	public //@ResponseBody Iterable<Dipendenti>
	   ModelAndView 
	   getAllDipendenti() {
	    // This returns a JSON or XML with the users
	    ModelAndView mav = new ModelAndView("lista-dipendenti");
		mav.addObject("dipendenti", dipendenteRepository.findDipendentiMatricolaAsc());
		return mav;
	}
	
	@GetMapping(path="/appalti")
	public ModelAndView getAllAppalti() {
	    // This returns a JSON or XML with the users
	    ModelAndView mav = new ModelAndView("lista-appalti");
	    Iterable<Appalto> listaAppalti = appaltoRepository.findAll(Sort.by("dataFine").ascending().and(Sort.by("dataInizio").ascending()));
	    //Iterable<Appalto> appalti = appaltoRepository.findApertiMatricolaNative(906); 
	    for(Appalto appalto: listaAppalti) {
	    	String cui = appalto.codiceCui;
	    	if(cui!=null) {
	    		if(cui.startsWith(K.prefissoCuiServizi) || cui.startsWith(K.prefissoCuiForniture))  {
	    	        List<ProgrammaServizi> programmaServizi = programmaServiziRepository.findByCui(cui);
	    	        appalto.serviziDiProgrammazione=programmaServizi;
	    		} else if(cui.startsWith(K.prefissoCuiLavori)) {
	    			List<ProgrammaLavori> programmaLavori = programmaLavoriRepository.findByCui(cui);
		    	    appalto.lavoriDiProgrammazione=programmaLavori;
	    		}
	       }
		}
		mav.addObject("appalti", listaAppalti);
		return mav;
	}
	
	@GetMapping(path="/programmazione-lavori")
	public ModelAndView getAllLavori() {
	    ModelAndView mav = new ModelAndView("programmazione-lavori");
	    Iterable<ProgrammaLavori> listaProgrammaLavori = programmaLavoriRepository.findAll(Sort.by("anno").ascending().and(Sort.by("meseavvioprocedura").ascending()));
	    		//programmaLavoriRepository.findLavoriNative();
	    for(ProgrammaLavori programmaLavori : listaProgrammaLavori) {
	    	String cui = programmaLavori.cui;
	    	List<Appalto> appalti = appaltoRepository.findByCodiceCui(cui);
	    	programmaLavori.appalti=appalti;
		}
		mav.addObject("programmaLavori", listaProgrammaLavori);
		return mav;
	}

	@GetMapping(path="/programmazione-servizi")
	public ModelAndView getAllServizi() {
	    ModelAndView mav = new ModelAndView("programmazione-servizi");
	    Iterable<ProgrammaServizi> listaProgrammaServizi = programmaServiziRepository.findAll(Sort.by("anno").ascending().and(Sort.by("meseavvioprocedura").ascending()));
	    for(ProgrammaServizi programmaServizi : listaProgrammaServizi) {
	    	String cui = programmaServizi.cui;
	    	List<Appalto> appalti = appaltoRepository.findByCodiceCui(cui);
	    	programmaServizi.appalti=appalti;
		}
		mav.addObject("programmaServizi", listaProgrammaServizi);
		return mav;
	}
	
	@GetMapping(path="/appalti/ballan")
	public ModelAndView getAllAppaltiBallan() {
	    // This returns a JSON or XML with the users
	    ModelAndView mav = new ModelAndView("lista-appalti-ballan");
	    //Iterable<Appalto> appalti = appaltoRepository.findAll(Sort.by("dataFine").ascending().and(Sort.by("dataInizio").ascending()));
	    Iterable<Appalto> appalti = appaltoRepository.findApertiMatricolaNative(906); 
		mav.addObject("appalti", appalti);
		return mav;
	}
	
	@GetMapping(path="/appalti/gallina")
	public ModelAndView getAllAppaltiGallina() {
	    // This returns a JSON or XML with the users
	    ModelAndView mav = new ModelAndView("lista-appalti-gallina");
	    Iterable<Appalto> appalti = appaltoRepository.findApertiMatricolaNative(10225); 
		mav.addObject("appalti", appalti);
		return mav;
	}
	
	@GetMapping(path="/appalti/consoletti")
	public ModelAndView getAllAppaltiConsoletti() {
	    // This returns a JSON or XML with the users
	    ModelAndView mav = new ModelAndView("lista-appalti-consoletti");
	    Iterable<Appalto> appalti = appaltoRepository.findApertiMatricolaNative(103138); 
		mav.addObject("appalti", appalti);
		return mav;
	}
}