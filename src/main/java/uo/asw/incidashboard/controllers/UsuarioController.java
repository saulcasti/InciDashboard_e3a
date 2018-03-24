package uo.asw.incidashboard.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uo.asw.dbManagement.model.Agente;
import uo.asw.dbManagement.model.Incidencia;
import uo.asw.dbManagement.model.Usuario;
import uo.asw.incidashboard.services.AgenteService;
import uo.asw.incidashboard.services.IncidenciaService;
import uo.asw.incidashboard.services.UsuarioService;

@Controller
public class UsuarioController {

	@Autowired
	private AgenteService agenteService;
	@Autowired
	private IncidenciaService incidenciaService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}
	
	
	@RequestMapping("/operarios/administrador" )
	public String getListado(Model model, Pageable pageable, Principal principal){
		
		String emailAgent = principal.getName();
		Agente agent = agenteService.getAgentByEmail(emailAgent);
		
		Page<Incidencia> incidencias = new PageImpl<Incidencia>(new LinkedList<Incidencia>());
		
		incidencias = incidenciaService.getIncidencias(pageable, agent.getId());
		
		model.addAttribute("incidenciasList", incidencias.getContent() );
		model.addAttribute("Agent", "Incidencias de "+agent.getNombre());
		model.addAttribute("page", incidencias);
		
		return "/operarios/administrador";
	}
	
	@RequestMapping(value="/mark/edit/{id}", method=RequestMethod.POST)
	public String setEdit(Model model, @PathVariable String nombre , @ModelAttribute Incidencia incidencia, Principal principal){
		Incidencia inciOr = incidenciaService.getIncidenciaByName(nombre);
		// modifica valor maximo y minimo
		inciOr.setMinimoValor(incidencia.getMinimoValor());
		inciOr.setMaximoValor(incidencia.getMaximoValor());
		incidenciaService.addIncidencia(inciOr);
		return "redirect:/operarios/administrador";
	}
}
