package clrobots;

import clrobots.EcoRobotAgents;
import clrobots.Environnement;
import clrobots.Forward;
import clrobots.GUI;
import clrobots.Launcher;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ITakeThreads;
import java.awt.Color;

@SuppressWarnings("all")
public abstract class ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  public interface Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> extends ScenarioEco.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public GUI.Component<UpdateOutput, ContextInit> gui();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Forward.Component<CycleAlert, Context, Actionable> fw();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Launcher.Component launcher();
  }
  
  public static class ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implements ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>, ScenarioEco.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    private final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> bridge;
    
    private final ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implementation;
    
    public void start() {
      assert this.environnement != null: "This is a bug.";
      ((Environnement.ComponentImpl<Actionable, Context, ContextInit, UpdateOutput>) this.environnement).start();
      assert this.gui != null: "This is a bug.";
      ((GUI.ComponentImpl<UpdateOutput, ContextInit>) this.gui).start();
      assert this.ecoAE != null: "This is a bug.";
      ((EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge>) this.ecoAE).start();
      assert this.fw != null: "This is a bug.";
      ((Forward.ComponentImpl<CycleAlert, Context, Actionable>) this.fw).start();
      assert this.launcher != null: "This is a bug.";
      ((Launcher.ComponentImpl) this.launcher).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_environnement() {
      assert this.environnement == null: "This is a bug.";
      assert this.implem_environnement == null: "This is a bug.";
      this.implem_environnement = this.implementation.make_environnement();
      if (this.implem_environnement == null) {
      	throw new RuntimeException("make_environnement() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.environnement = this.implem_environnement._newComponent(new BridgeImpl_environnement(), false);
      
    }
    
    private void init_gui() {
      assert this.gui == null: "This is a bug.";
      assert this.implem_gui == null: "This is a bug.";
      this.implem_gui = this.implementation.make_gui();
      if (this.implem_gui == null) {
      	throw new RuntimeException("make_gui() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.gui = this.implem_gui._newComponent(new BridgeImpl_gui(), false);
      
    }
    
    private void init_ecoAE() {
      assert this.ecoAE == null: "This is a bug.";
      assert this.implem_ecoAE == null: "This is a bug.";
      this.implem_ecoAE = this.implementation.make_ecoAE();
      if (this.implem_ecoAE == null) {
      	throw new RuntimeException("make_ecoAE() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.ecoAE = this.implem_ecoAE._newComponent(new BridgeImpl_ecoAE(), false);
      
    }
    
    private void init_fw() {
      assert this.fw == null: "This is a bug.";
      assert this.implem_fw == null: "This is a bug.";
      this.implem_fw = this.implementation.make_fw();
      if (this.implem_fw == null) {
      	throw new RuntimeException("make_fw() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.fw = this.implem_fw._newComponent(new BridgeImpl_fw(), false);
      
    }
    
    private void init_launcher() {
      assert this.launcher == null: "This is a bug.";
      assert this.implem_launcher == null: "This is a bug.";
      this.implem_launcher = this.implementation.make_launcher();
      if (this.implem_launcher == null) {
      	throw new RuntimeException("make_launcher() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.launcher = this.implem_launcher._newComponent(new BridgeImpl_launcher(), false);
      
    }
    
    protected void initParts() {
      init_environnement();
      init_gui();
      init_ecoAE();
      init_fw();
      init_launcher();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implem, final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement;
    
    private Environnement<Actionable, Context, ContextInit, UpdateOutput> implem_environnement;
    
    private final class BridgeImpl_environnement implements Environnement.Requires<Actionable, Context, ContextInit, UpdateOutput> {
      public final UpdateOutput updateOutput() {
        return ScenarioEco.ComponentImpl.this.gui().updateGUI();
      }
    }
    
    public final Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement() {
      return this.environnement;
    }
    
    private GUI.Component<UpdateOutput, ContextInit> gui;
    
    private GUI<UpdateOutput, ContextInit> implem_gui;
    
    private final class BridgeImpl_gui implements GUI.Requires<UpdateOutput, ContextInit> {
      public final ContextInit initEnvironnement() {
        return ScenarioEco.ComponentImpl.this.environnement().envInit();
      }
    }
    
    public final GUI.Component<UpdateOutput, ContextInit> gui() {
      return this.gui;
    }
    
    private EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE;
    
    private EcoRobotAgents<Actionable, Context, SelfKnowledge> implem_ecoAE;
    
    private final class BridgeImpl_ecoAE implements EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge> {
      public final ITakeThreads threads() {
        return ScenarioEco.ComponentImpl.this.launcher().threads();
      }
    }
    
    public final EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE() {
      return this.ecoAE;
    }
    
    private Forward.Component<CycleAlert, Context, Actionable> fw;
    
    private Forward<CycleAlert, Context, Actionable> implem_fw;
    
    private final class BridgeImpl_fw implements Forward.Requires<CycleAlert, Context, Actionable> {
      public final CycleAlert i() {
        return ScenarioEco.ComponentImpl.this.launcher().finishedCycle();
      }
      
      public final Context j() {
        return ScenarioEco.ComponentImpl.this.environnement().envInfos();
      }
      
      public final Actionable k() {
        return ScenarioEco.ComponentImpl.this.environnement().interagir();
      }
    }
    
    public final Forward.Component<CycleAlert, Context, Actionable> fw() {
      return this.fw;
    }
    
    private Launcher.Component launcher;
    
    private Launcher implem_launcher;
    
    private final class BridgeImpl_launcher implements Launcher.Requires {
    }
    
    public final Launcher.Component launcher() {
      return this.launcher;
    }
  }
  
  public static class DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    public interface Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    }
    
    public interface Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> extends ScenarioEco.DynamicAssembly.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    }
    
    public interface Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    }
    
    public interface Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Forward.Agent.Component<CycleAlert, Context, Actionable> aFW();
    }
    
    public static class ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implements ScenarioEco.DynamicAssembly.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>, ScenarioEco.DynamicAssembly.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
      private final ScenarioEco.DynamicAssembly.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> bridge;
      
      private final ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implementation;
      
      public void start() {
        assert this.agentE != null: "This is a bug.";
        ((EcoRobotAgents.Robot.ComponentImpl<Actionable, Context, SelfKnowledge>) this.agentE).start();
        assert this.aFW != null: "This is a bug.";
        ((Forward.Agent.ComponentImpl<CycleAlert, Context, Actionable>) this.aFW).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_agentE() {
        assert this.agentE == null: "This is a bug.";
        assert this.implementation.use_agentE != null: "This is a bug.";
        this.agentE = this.implementation.use_agentE._newComponent(new BridgeImpl_ecoAE_agentE(), false);
        
      }
      
      private void init_aFW() {
        assert this.aFW == null: "This is a bug.";
        assert this.implementation.use_aFW != null: "This is a bug.";
        this.aFW = this.implementation.use_aFW._newComponent(new BridgeImpl_fw_aFW(), false);
        
      }
      
      protected void initParts() {
        init_agentE();
        init_aFW();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implem, final ScenarioEco.DynamicAssembly.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean doInits) {
        this.bridge = b;
        this.implementation = implem;
        
        assert implem.selfComponent == null: "This is a bug.";
        implem.selfComponent = this;
        
        // prevent them to be called twice if we are in
        // a specialized component: only the last of the
        // hierarchy will call them after everything is initialised
        if (doInits) {
        	initParts();
        	initProvidedPorts();
        }
      }
      
      private EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE;
      
      private final class BridgeImpl_ecoAE_agentE implements EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge> {
        public final CycleAlert finishedCycle() {
          return ScenarioEco.DynamicAssembly.ComponentImpl.this.aFW().a();
        }
        
        public final Actionable envInteraction() {
          return ScenarioEco.DynamicAssembly.ComponentImpl.this.aFW().c();
        }
        
        public final Context envContext() {
          return ScenarioEco.DynamicAssembly.ComponentImpl.this.aFW().b();
        }
      }
      
      public final EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE() {
        return this.agentE;
      }
      
      private Forward.Agent.Component<CycleAlert, Context, Actionable> aFW;
      
      private final class BridgeImpl_fw_aFW implements Forward.Agent.Requires<CycleAlert, Context, Actionable> {
      }
      
      public final Forward.Agent.Component<CycleAlert, Context, Actionable> aFW() {
        return this.aFW;
      }
    }
    
    /**
     * Used to check that two components are not created from the same implementation,
     * that the component has been started to call requires(), provides() and parts()
     * and that the component is not started by hand.
     * 
     */
    private boolean init = false;;
    
    /**
     * Used to check that the component is not started by hand.
     * 
     */
    private boolean started = false;;
    
    private ScenarioEco.DynamicAssembly.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> selfComponent;
    
    /**
     * Can be overridden by the implementation.
     * It will be called automatically after the component has been instantiated.
     * 
     */
    protected void start() {
      if (!this.init || this.started) {
      	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
      }
    }
    
    /**
     * This can be called by the implementation to access the provided ports.
     * 
     */
    protected ScenarioEco.DynamicAssembly.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected ScenarioEco.DynamicAssembly.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> requires() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
      }
      return this.selfComponent.bridge;
    }
    
    /**
     * This can be called by the implementation to access the parts and their provided ports.
     * 
     */
    protected ScenarioEco.DynamicAssembly.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge> use_agentE;
    
    private Forward.Agent<CycleAlert, Context, Actionable> use_aFW;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized ScenarioEco.DynamicAssembly.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> _newComponent(final ScenarioEco.DynamicAssembly.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembly has already been used to create a component, use another one.");
      }
      this.init = true;
      ScenarioEco.DynamicAssembly.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>  _comp = new ScenarioEco.DynamicAssembly.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected ScenarioEco.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected ScenarioEco.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected ScenarioEco.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected ScenarioEco.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Environnement<Actionable, Context, ContextInit, UpdateOutput> make_environnement();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract GUI<UpdateOutput, ContextInit> make_gui();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract EcoRobotAgents<Actionable, Context, SelfKnowledge> make_ecoAE();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Forward<CycleAlert, Context, Actionable> make_fw();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Launcher make_launcher();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> _newComponent(final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ScenarioEco has already been used to create a component, use another one.");
    }
    this.init = true;
    ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>  _comp = new ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> make_DynamicAssembly(final String id, final Color color) {
    return new ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> _createImplementationOfDynamicAssembly(final String id, final Color color) {
    ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implem = make_DynamicAssembly(id,color);
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembly() in clrobots.ScenarioEco should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoAE != null: "This is a bug.";
    assert implem.use_agentE == null: "This is a bug.";
    implem.use_agentE = this.selfComponent.implem_ecoAE._createImplementationOfRobot(id,color);
    assert this.selfComponent.implem_fw != null: "This is a bug.";
    assert implem.use_aFW == null: "This is a bug.";
    implem.use_aFW = this.selfComponent.implem_fw._createImplementationOfAgent();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected ScenarioEco.DynamicAssembly.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> newDynamicAssembly(final String id, final Color color) {
    ScenarioEco.DynamicAssembly<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> _implem = _createImplementationOfDynamicAssembly(id,color);
    return _implem._newComponent(new ScenarioEco.DynamicAssembly.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> newComponent() {
    return this._newComponent(new ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>() {}, true);
  }
}
