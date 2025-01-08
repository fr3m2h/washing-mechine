package com.emse.washing_mechine.hello;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DummyUserService implements UserService
{
    // Injection
    private final GreetingService greetservice;
    //Injection.1
    public DummyUserService(GreetingService greetservice)
    {
        this.greetservice=greetservice;
    }
    //Injection.2
    public GreetingService getGreetingService()
    {
        return this.greetservice;
    }
    // Méthode greetAll
    public void greetAll(List<String> names)
    {
        for(int i=0;i<names.size();i++)
        {
            greetservice.greet(names.get(i));
        }
    }
}

