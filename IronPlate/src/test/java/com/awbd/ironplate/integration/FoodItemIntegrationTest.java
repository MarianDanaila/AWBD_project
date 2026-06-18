package com.awbd.ironplate.integration;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.repository.FoodItemRepository;
import com.awbd.ironplate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FoodItemIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        foodItemRepository.deleteAll();
    }

    // Scenario 1: Any authenticated user can list food items
    @Test
    @WithMockUser(username = "athlete", roles = "ATHLETE")
    void listFoodItems_shouldReturn200_forAnyAuthenticatedUser() throws Exception {
        FoodItem item = new FoodItem();
        item.setName("Banana");
        item.setCategory(FoodItem.FoodCategory.FRUIT);
        item.setCaloriesPer100g(89.0);
        foodItemRepository.save(item);

        mockMvc.perform(get("/food-items"))
                .andExpect(status().isOk())
                .andExpect(view().name("food-items/list"))
                .andExpect(model().attributeExists("foodItems"));
    }

    // Scenario 2: COACH can create a new food item end-to-end
    @Test
    void createFoodItem_shouldPersistAndRedirect_forCoach() throws Exception {
        mockMvc.perform(post("/food-items/new")
                        .with(user("coach").roles("COACH"))
                        .with(csrf())
                        .param("name", "Brown Rice")
                        .param("category", "CARBOHYDRATE")
                        .param("caloriesPer100g", "112")
                        .param("proteinPer100g", "2.6")
                        .param("carbsPer100g", "23.5")
                        .param("fatPer100g", "0.9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food-items"));

        assertThat(foodItemRepository.findByNameContainingIgnoreCase("Brown Rice",
                org.springframework.data.domain.PageRequest.of(0, 10))).isNotEmpty();
    }

    // Scenario 3: ATHLETE is forbidden from creating food items
    @Test
    void createFoodItem_shouldBeForbidden_forAthlete() throws Exception {
        mockMvc.perform(post("/food-items/new")
                        .with(user("athlete").roles("ATHLETE"))
                        .with(csrf())
                        .param("name", "Forbidden Item")
                        .param("category", "FRUIT")
                        .param("caloriesPer100g", "50"))
                .andExpect(status().isForbidden());
    }
}
