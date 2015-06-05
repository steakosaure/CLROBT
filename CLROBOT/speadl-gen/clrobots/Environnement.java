package clrobots;

@SuppressWarnings("all")
public abstract class Environnement<Actionable, Context, ContextInit> {
  public interface Requires<Actionable, Context, ContextInit> {
  }
  
  public interface Component<Actionable, Context, ContextInit> extends Environnement.Provides<Actionable, Context, ContextInit> {
  }
  
  public interface Provides<Actionable, Context, ContextInit> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public Actionable interagir();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public Context envInfos();
    
    /**
     * This can be called to access the provided port.
     * 
     */
    public ContextInit envInit();
  }
  
  public interface Parts<Actionable, Context, ContextInit> {
  }
  
  public static class ComponentImpl<Actionable, Context, ContextInit> implements Environnement.Component<Actionable, Context, ContextInit>, Environnement.Parts<Actionable, Context, ContextInit> {
    private final Environnement.Requires<Actionable, Context, ContextInit> bridge;
    
    private final Environnement<Actionable, Context, ContextInit> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_interagir() {
      assert this.interagir == null: "This is a bug.";
      this.interagir = this.implementation.make_interagir();
      if (this.interagir == null) {
      	throw new RuntimeException("make_interagir() in clrobots.Environnement<Actionable, Context, ContextInit> should not return null.");
      }
    }
    
    private void init_envInfos() {
      assert this.envInfos == null: "This is a bug.";
      this.envInfos = this.implementation.make_envInfos();
      if (this.envInfos == null) {
      	throw new RuntimeException("make_envInfos() in clrobots.Environnement<Actionable, Context, ContextInit> should not return null.");
      }
    }
    
    private void init_envInit() {
      assert this.envInit == null: "This is a bug.";
      this.envInit = this.implementation.make_envInit();
      if (this.envInit == null) {
      	throw new RuntimeException("make_envInit() in clrobots.Environnement<Actionable, Context, ContextInit> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_interagir();
      init_envInfos();
      init_envInit();
    }
    
    public ComponentImpl(final Environnement<Actionable, Context, ContextInit> implem, final Environnement.Requires<Actionable, Context, ContextInit> b, final boolean doInits) {
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
    
    private Actionable interagir;
    
    public Actionable interagir() {
      return this.interagir;
    }
    
    private Context envInfos;
    
    public Context envInfos() {
      return this.envInfos;
    }
    
    private ContextInit envInit;
    
    public ContextInit envInit() {
      return this.envInit;
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
  
  private Environnement.ComponentImpl<Actionable, Context, ContextInit> selfComponent;
  
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
  protected Environnement.Provides<Actionable, Context, ContextInit> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract Actionable make_interagir();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract Context make_envInfos();
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ContextInit make_envInit();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected Environnement.Requires<Actionable, Context, ContextInit> requires() {
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
  protected Environnement.Parts<Actionable, Context, ContextInit> parts() {
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
  public synchronized Environnement.Component<Actionable, Context, ContextInit> _newComponent(final Environnement.Requires<Actionable, Context, ContextInit> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Environnement has already been used to create a component, use another one.");
    }
    this.init = true;
    Environnement.ComponentImpl<Actionable, Context, ContextInit>  _comp = new Environnement.ComponentImpl<Actionable, Context, ContextInit>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public Environnement.Component<Actionable, Context, ContextInit> newComponent() {
    return this._newComponent(new Environnement.Requires<Actionable, Context, ContextInit>() {}, true);
  }
}
