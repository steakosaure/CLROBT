package clrobots;

import clrobots.Agir;
import clrobots.Decider;
import clrobots.Knowledge;
import clrobots.Percevoir;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.Do;
import clrobots.interfaces.ITakeThreads;
import environnement.Cellule;
import java.awt.Color;
import java.awt.Point;
import java.util.Map;

@SuppressWarnings("all")
public abstract class EcoRobotAgents<Actionable, Context, SelfKnowledge, Push, Pull> {
  public interface Requires<Actionable, Context, SelfKnowledge, Push, Pull> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ITakeThreads threads();
  }
  
  public interface Component<Actionable, Context, SelfKnowledge, Push, Pull> extends EcoRobotAgents.Provides<Actionable, Context, SelfKnowledge, Push, Pull> {
  }
  
  public interface Provides<Actionable, Context, SelfKnowledge, Push, Pull> {
  }
  
  public interface Parts<Actionable, Context, SelfKnowledge, Push, Pull> {
  }
  
  public static class ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull> implements EcoRobotAgents.Component<Actionable, Context, SelfKnowledge, Push, Pull>, EcoRobotAgents.Parts<Actionable, Context, SelfKnowledge, Push, Pull> {
    private final EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge, Push, Pull> bridge;
    
    private final EcoRobotAgents<Actionable, Context, SelfKnowledge, Push, Pull> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final EcoRobotAgents<Actionable, Context, SelfKnowledge, Push, Pull> implem, final EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge, Push, Pull> b, final boolean doInits) {
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
  }
  
  public static abstract class Robot<Actionable, Context, SelfKnowledge, Push, Pull> {
    public interface Requires<Actionable, Context, SelfKnowledge, Push, Pull> {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Push pushMessage();
      
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Pull pullMessage();
      
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Actionable envInteraction();
      
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public Context envContext();
      
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public CycleAlert finishedCycle();
    }
    
    public interface Component<Actionable, Context, SelfKnowledge, Push, Pull> extends EcoRobotAgents.Robot.Provides<Actionable, Context, SelfKnowledge, Push, Pull> {
    }
    
    public interface Provides<Actionable, Context, SelfKnowledge, Push, Pull> {
      /**
       * This can be called to access the provided port.
       * 
       */
      public Do launchCycle();
    }
    
    public interface Parts<Actionable, Context, SelfKnowledge, Push, Pull> {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Knowledge.Component<SelfKnowledge> knowledge();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Percevoir.Component<Context, SelfKnowledge, Pull> percevoir();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Decider.Component<Actionable, SelfKnowledge, Push> decider();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Agir.Component<Actionable, SelfKnowledge, Push> agir();
    }
    
    public static class ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull> implements EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge, Push, Pull>, EcoRobotAgents.Robot.Parts<Actionable, Context, SelfKnowledge, Push, Pull> {
      private final EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge, Push, Pull> bridge;
      
      private final EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge, Push, Pull> implementation;
      
      public void start() {
        assert this.knowledge != null: "This is a bug.";
        ((Knowledge.ComponentImpl<SelfKnowledge>) this.knowledge).start();
        assert this.percevoir != null: "This is a bug.";
        ((Percevoir.ComponentImpl<Context, SelfKnowledge, Pull>) this.percevoir).start();
        assert this.decider != null: "This is a bug.";
        ((Decider.ComponentImpl<Actionable, SelfKnowledge, Push>) this.decider).start();
        assert this.agir != null: "This is a bug.";
        ((Agir.ComponentImpl<Actionable, SelfKnowledge, Push>) this.agir).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_knowledge() {
        assert this.knowledge == null: "This is a bug.";
        assert this.implem_knowledge == null: "This is a bug.";
        this.implem_knowledge = this.implementation.make_knowledge();
        if (this.implem_knowledge == null) {
        	throw new RuntimeException("make_knowledge() in clrobots.EcoRobotAgents$Robot<Actionable, Context, SelfKnowledge, Push, Pull> should not return null.");
        }
        this.knowledge = this.implem_knowledge._newComponent(new BridgeImpl_knowledge(), false);
        
      }
      
      private void init_percevoir() {
        assert this.percevoir == null: "This is a bug.";
        assert this.implem_percevoir == null: "This is a bug.";
        this.implem_percevoir = this.implementation.make_percevoir();
        if (this.implem_percevoir == null) {
        	throw new RuntimeException("make_percevoir() in clrobots.EcoRobotAgents$Robot<Actionable, Context, SelfKnowledge, Push, Pull> should not return null.");
        }
        this.percevoir = this.implem_percevoir._newComponent(new BridgeImpl_percevoir(), false);
        
      }
      
      private void init_decider() {
        assert this.decider == null: "This is a bug.";
        assert this.implem_decider == null: "This is a bug.";
        this.implem_decider = this.implementation.make_decider();
        if (this.implem_decider == null) {
        	throw new RuntimeException("make_decider() in clrobots.EcoRobotAgents$Robot<Actionable, Context, SelfKnowledge, Push, Pull> should not return null.");
        }
        this.decider = this.implem_decider._newComponent(new BridgeImpl_decider(), false);
        
      }
      
      private void init_agir() {
        assert this.agir == null: "This is a bug.";
        assert this.implem_agir == null: "This is a bug.";
        this.implem_agir = this.implementation.make_agir();
        if (this.implem_agir == null) {
        	throw new RuntimeException("make_agir() in clrobots.EcoRobotAgents$Robot<Actionable, Context, SelfKnowledge, Push, Pull> should not return null.");
        }
        this.agir = this.implem_agir._newComponent(new BridgeImpl_agir(), false);
        
      }
      
      protected void initParts() {
        init_knowledge();
        init_percevoir();
        init_decider();
        init_agir();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge, Push, Pull> implem, final EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge, Push, Pull> b, final boolean doInits) {
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
      
      public Do launchCycle() {
        return this.percevoir().perception();
      }
      
      private Knowledge.Component<SelfKnowledge> knowledge;
      
      private Knowledge<SelfKnowledge> implem_knowledge;
      
      private final class BridgeImpl_knowledge implements Knowledge.Requires<SelfKnowledge> {
      }
      
      public final Knowledge.Component<SelfKnowledge> knowledge() {
        return this.knowledge;
      }
      
      private Percevoir.Component<Context, SelfKnowledge, Pull> percevoir;
      
      private Percevoir<Context, SelfKnowledge, Pull> implem_percevoir;
      
      private final class BridgeImpl_percevoir implements Percevoir.Requires<Context, SelfKnowledge, Pull> {
        public final Do decision() {
          return EcoRobotAgents.Robot.ComponentImpl.this.decider().decision();
        }
        
        public final SelfKnowledge knowledge() {
          return EcoRobotAgents.Robot.ComponentImpl.this.knowledge().selfKnowledge();
        }
        
        public final Context context() {
          return EcoRobotAgents.Robot.ComponentImpl.this.bridge.envContext();
        }
        
        public final Pull getMessage() {
          return EcoRobotAgents.Robot.ComponentImpl.this.bridge.pullMessage();
        }
      }
      
      public final Percevoir.Component<Context, SelfKnowledge, Pull> percevoir() {
        return this.percevoir;
      }
      
      private Decider.Component<Actionable, SelfKnowledge, Push> decider;
      
      private Decider<Actionable, SelfKnowledge, Push> implem_decider;
      
      private final class BridgeImpl_decider implements Decider.Requires<Actionable, SelfKnowledge, Push> {
        public final Actionable action() {
          return EcoRobotAgents.Robot.ComponentImpl.this.agir().action();
        }
        
        public final SelfKnowledge knowledge() {
          return EcoRobotAgents.Robot.ComponentImpl.this.knowledge().selfKnowledge();
        }
        
        public final Push sendMessage() {
          return EcoRobotAgents.Robot.ComponentImpl.this.agir().sendMessage();
        }
      }
      
      public final Decider.Component<Actionable, SelfKnowledge, Push> decider() {
        return this.decider;
      }
      
      private Agir.Component<Actionable, SelfKnowledge, Push> agir;
      
      private Agir<Actionable, SelfKnowledge, Push> implem_agir;
      
      private final class BridgeImpl_agir implements Agir.Requires<Actionable, SelfKnowledge, Push> {
        public final CycleAlert finishedCycle() {
          return EcoRobotAgents.Robot.ComponentImpl.this.bridge.finishedCycle();
        }
        
        public final Actionable interagir() {
          return EcoRobotAgents.Robot.ComponentImpl.this.bridge.envInteraction();
        }
        
        public final SelfKnowledge knowledge() {
          return EcoRobotAgents.Robot.ComponentImpl.this.knowledge().selfKnowledge();
        }
        
        public final Push push() {
          return EcoRobotAgents.Robot.ComponentImpl.this.bridge.pushMessage();
        }
      }
      
      public final Agir.Component<Actionable, SelfKnowledge, Push> agir() {
        return this.agir;
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
    
    private EcoRobotAgents.Robot.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull> selfComponent;
    
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
    protected EcoRobotAgents.Robot.Provides<Actionable, Context, SelfKnowledge, Push, Pull> provides() {
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
    protected EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge, Push, Pull> requires() {
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
    protected EcoRobotAgents.Robot.Parts<Actionable, Context, SelfKnowledge, Push, Pull> parts() {
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
    protected abstract Knowledge<SelfKnowledge> make_knowledge();
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract Percevoir<Context, SelfKnowledge, Pull> make_percevoir();
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract Decider<Actionable, SelfKnowledge, Push> make_decider();
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract Agir<Actionable, SelfKnowledge, Push> make_agir();
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge, Push, Pull> _newComponent(final EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge, Push, Pull> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Robot has already been used to create a component, use another one.");
      }
      this.init = true;
      EcoRobotAgents.Robot.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull>  _comp = new EcoRobotAgents.Robot.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EcoRobotAgents.Provides<Actionable, Context, SelfKnowledge, Push, Pull> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge, Push, Pull> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EcoRobotAgents.Parts<Actionable, Context, SelfKnowledge, Push, Pull> eco_parts() {
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
  
  private EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull> selfComponent;
  
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
  protected EcoRobotAgents.Provides<Actionable, Context, SelfKnowledge, Push, Pull> provides() {
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
  protected EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge, Push, Pull> requires() {
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
  protected EcoRobotAgents.Parts<Actionable, Context, SelfKnowledge, Push, Pull> parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized EcoRobotAgents.Component<Actionable, Context, SelfKnowledge, Push, Pull> _newComponent(final EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge, Push, Pull> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EcoRobotAgents has already been used to create a component, use another one.");
    }
    this.init = true;
    EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull>  _comp = new EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge, Push, Pull>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge, Push, Pull> make_Robot(final String id, final Color color, final Cellule position, final Map<Color, Point> nests);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge, Push, Pull> _createImplementationOfRobot(final String id, final Color color, final Cellule position, final Map<Color, Point> nests) {
    EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge, Push, Pull> implem = make_Robot(id,color,position,nests);
    if (implem == null) {
    	throw new RuntimeException("make_Robot() in clrobots.EcoRobotAgents should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
}
