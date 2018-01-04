package de.kintel.ki.gui;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MainViewModel implements ViewModel {

    @InjectViewModel
    MainViewModel viewModel;

}
