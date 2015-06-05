package clrobots;

@SuppressWarnings("all")
public abstract class GUI<UpdateGUI, ContextInfos> {
  public interface Requires<UpdateGUI, ContextInfos> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ContextInfos initEnvironnement();
  }
  
  public interface Component<UpdateGUI, ContextInfos> extends GUI.Provides<UpdateGUI, ContextInfos> {
  }
  
  public interface Provides<UpdateGUI, ContextInfos> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public UpdateGUI updateGUI();
  }
  
  public interface Parts<UpdateGUI, ContextInfos> {
  }
  
  public static class ComponentImpl<UpdateGUI, ContextInfos> implements GUI.Component<UpdateGUI, ContextInfos>, GUI.Parts<UpdateGUI, ContextInfos> {
    private final GUI.Requires<UpdateGUI, ContextInfos> bridge;
    
    private final GUI<UpdateGUI, ContextInfos> implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    private void init_updateGUI() {
      assert this.updateGUI == null: "This is a bug.";
      this.updateGUI = this.implementation.make_updateGUI();
      if (this.updateGUI == null) {
      	throw new RuntimeException("make_updateGUI() in clrobots.GUI<UpdateGUI, ContextInfos> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_updateGUI();
    }
    
    public ComponentImpl(final GUI<UpdateGUI, ContextInfos> implem, final GUI.Requires<UpdateGUI, ContextInfos> b, final boolean doInits) {
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
    
    private UpdateGUI updateGUI;
    
    public UpdateGUI updateGUI() {
      return this.updateGUI;
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
  
  private GUI.ComponentImpl<UpdateGUI, ContextInfos> selfComponent;
  
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
  protected GUI.Provides<UpdateGUI, ContextInfos> provides() {
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
  protected abstract UpdateGUI make_updateGUI();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected GUI.Requires<UpdateGUI, ContextInfos> requires() {
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
  protected GUI.Parts<UpdateGUI, ContextInfos> parts() {
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
  public synchronized GUI.Component<UpdateGUI, ContextInfos> _newComponent(final GUI.Requires<UpdateGUI, ContextInfos> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of GUI has already been used to create a component, use another one.");
    }
    this.init = true;
    GUI.ComponentImpl<UpdateGUI, ContextInfos>  _comp = new GUI.ComponentImpl<UpdateGUI, ContextInfos>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
}
