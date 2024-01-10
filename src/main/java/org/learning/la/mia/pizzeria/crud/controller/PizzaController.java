package org.learning.la.mia.pizzeria.crud.controller;

import jakarta.validation.Valid;
import org.learning.la.mia.pizzeria.crud.interfaccie.PizzeriaRepository;
import org.learning.la.mia.pizzeria.crud.model.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzeriaRepository pizzeriaRepository;

    @GetMapping
    public String index(Model model) {
        List<Pizza> pizzaList = pizzeriaRepository.findAll();
        model.addAttribute("pizzaList", pizzaList);
        return "pizzas/list";
    }

    @GetMapping("/show/{name}")
    public String show(@PathVariable String name, Model model) {
        Optional<Pizza> result = pizzeriaRepository.findById(name);
        if (result.isPresent()) {
            Pizza pizza = result.get();
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza " + name + " not found");
        }

    }

    @GetMapping("/newPizza")
    public String create(Model model) {
        Pizza pizza = new Pizza();
        model.addAttribute("pizza", pizza);
        return "pizzas/newPizza";
    }

    @PostMapping("/newPizza")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pizzas/newPizza";
        } else {
            Pizza savePizza = pizzeriaRepository.save(formPizza);
            return "redirect:/pizzas/list/" + savePizza.getName();
        }

    }


}
