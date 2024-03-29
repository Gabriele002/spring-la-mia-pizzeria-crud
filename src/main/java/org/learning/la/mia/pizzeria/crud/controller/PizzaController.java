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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Pizza> result = pizzeriaRepository.findById(id);
        if (result.isPresent()) {
            Pizza pizza = result.get();
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza " + id + " not found");
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
            return "redirect:/pizzas";
        }

    }

    @GetMapping("/editPizza/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Pizza> result = pizzeriaRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("pizza", result.get());
            return "pizzas/editPizza";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

    @PostMapping("/editPizza/{id}")
    public String updatepizza (@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza formPizza,  BindingResult bindingResult ) {
        Optional<Pizza> pizza = pizzeriaRepository.findById(formPizza.getId());
        if (pizza.isPresent()) {
            Pizza pizzaedit = pizza.get();
            if (bindingResult.hasErrors()) {
                return "pizzas/editPizza";
            }
            formPizza.setPhoto(pizzaedit.getPhoto());
            Pizza savedpizza = pizzeriaRepository.save(formPizza);
            return "redirect:/pizzas";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        Optional<Pizza> result = pizzeriaRepository.findById(id);
        if (result.isPresent()){
            pizzeriaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("redirectMessage","Pizza" + result.get().getName() + "deleted!");
            return "redirect:/pizzas";
        } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

}
